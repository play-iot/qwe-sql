package io.github.zero88.qwe.sql;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;

import org.jooq.Configuration;
import org.jooq.impl.DefaultConfiguration;

import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.component.Component;
import io.github.zero88.qwe.component.ComponentVerticle;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.ErrorMessage;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.exceptions.InitializerError;
import io.github.zero88.qwe.exceptions.InitializerError.MigrationError;
import io.github.zero88.qwe.exceptions.converter.CarlExceptionConverter;
import io.github.zero88.qwe.sql.SqlContext.CreationHandler;
import io.github.zero88.qwe.sql.handler.EntityHandler;
import io.github.zero88.qwe.utils.ExecutorHelpers;
import io.github.zero88.utils.Reflections.ReflectionClass;
import io.reactivex.Single;
import io.vertx.core.Promise;

import com.zaxxer.hikari.HikariDataSource;

import lombok.NonNull;

public final class SqlVerticle<T extends EntityHandler> extends ComponentVerticle<SqlConfig, SqlContext<T>> {

    private final Class<T> entityHandlerClass;
    private DataSource dataSource;
    private T handler;

    public SqlVerticle(SharedDataLocalProxy sharedData, Class<T> handlerClass) {
        super(sharedData);
        this.entityHandlerClass = handlerClass;
    }

    @Override
    public void start() {
        super.start();
        config.getHikariConfig()
              .setJdbcUrl(config.computeJdbcUrl(() -> sharedData().getData(SharedDataLocalProxy.APP_DATADIR)));
        if (logger.isDebugEnabled()) {
            logger.debug("SQL config: {}", config.getHikariConfig().toJson());
        }
    }

    @Override
    public void start(Promise<Void> future) {
        this.start();
        logger.info("Creating Hikari datasource from application configuration...");
        ExecutorHelpers.blocking(vertx, () -> this.dataSource = new HikariDataSource(config.getHikariConfig()))
                       .map(ds -> new DefaultConfiguration().set(ds).set(config.getDialect()))
                       .flatMap(this::createSchemaThenData)
                       .map(this::validateInitOrMigrationData)
                       .subscribe(result -> complete(future, result), t -> future.fail(CarlExceptionConverter.from(t)));
    }

    @Override
    public void stop() {
        try {
            this.dataSource.unwrap(HikariDataSource.class).close();
        } catch (SQLException e) {
            logger.info("Unable to close datasource", e);
        }
    }

    @Override
    public Class<SqlConfig> configClass() { return SqlConfig.class; }

    @Override
    public SqlContext<T> onSuccess(@NonNull Class<Component<IConfig, SqlContext<T>>> aClass, Path dataDir,
                                   String sharedKey, String deployId) {
        return new SqlContext<>(aClass, dataDir, sharedKey, deployId, this.handler);
    }

    @Override
    public String configFile() { return "sql.json"; }

    private void complete(Promise<Void> future, EventMessage result) {
        logger.info("{} SQL verticle {}", result.getAction(), result.toJson());
        if (result.isSuccess()) {
            logger.info("DATABASE IS READY TO USE");
        }
        future.complete();
    }

    private Single<EventMessage> createSchemaThenData(Configuration jooqConfig) {
        this.handler = createHandler(jooqConfig);
        return handler.before()
                      .map(EntityHandler::schemaHandler)
                      .flatMap(schemaHandler -> schemaHandler.execute(handler))
                      .onErrorResumeNext(throwable -> Single.error(
                          new InitializerError("Unknown error when initializing database", throwable)));
    }

    private EventMessage validateInitOrMigrationData(EventMessage result) {
        if (!result.isError()) {
            return result;
        }
        ErrorMessage error = result.getError();
        Throwable t = error.getThrowable();
        if (Objects.isNull(t)) {
            t = new CarlException(error.getCode(), error.getMessage());
        }
        if (result.getAction() == EventAction.INIT) {
            throw new InitializerError("Failed to startup SQL component", t);
        } else {
            throw new MigrationError("Failed to startup SQL component", t);
        }
    }

    private T createHandler(@NonNull Configuration configuration) {
        Map<Class, Object> map = new LinkedHashMap<>();
        map.put(Configuration.class, configuration);
        map.put(SharedDataLocalProxy.class, sharedData());
        return ReflectionClass.createObject(entityHandlerClass, map, new CreationHandler<>()).get();
    }

}
