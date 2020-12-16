package io.github.zero88.msa.sql;

import java.sql.SQLException;
import java.util.Objects;
import javax.sql.DataSource;

import org.jooq.Configuration;
import org.jooq.impl.DefaultConfiguration;

import io.github.zero88.msa.bp.component.SharedDataDelegate;
import io.github.zero88.msa.bp.component.UnitVerticle;
import io.github.zero88.msa.bp.dto.ErrorMessage;
import io.github.zero88.msa.bp.event.EventAction;
import io.github.zero88.msa.bp.event.EventMessage;
import io.github.zero88.msa.bp.exceptions.BlueprintException;
import io.github.zero88.msa.bp.exceptions.InitializerError;
import io.github.zero88.msa.bp.exceptions.InitializerError.MigrationError;
import io.github.zero88.msa.bp.exceptions.converter.BlueprintExceptionConverter;
import io.github.zero88.msa.bp.utils.ExecutorHelpers;
import io.github.zero88.msa.sql.handler.EntityHandler;
import io.reactivex.Single;
import io.vertx.core.Future;

import com.zaxxer.hikari.HikariDataSource;

public final class SqlVerticle<T extends EntityHandler> extends UnitVerticle<SqlConfig, SqlContext<T>> {

    private DataSource dataSource;

    SqlVerticle(Class<T> handlerClass) {
        super(new SqlContext<>(handlerClass));
    }

    @Override
    public void start() {
        super.start();
        config.getHikariConfig()
              .setJdbcUrl(config.computeJdbcUrl(() -> getSharedData(SharedDataDelegate.SHARED_DATADIR)));
        if (logger.isDebugEnabled()) {
            logger.debug(config.getHikariConfig().toJson());
        }
    }

    @Override
    public void start(Future<Void> future) {
        this.start();
        logger.info("Creating Hikari datasource from application configuration...");
        ExecutorHelpers.blocking(vertx, () -> this.dataSource = new HikariDataSource(config.getHikariConfig()))
                       .map(ds -> new DefaultConfiguration().set(ds).set(config.getDialect()))
                       .flatMap(this::createSchemaThenData)
                       .map(this::validateInitOrMigrationData)
                       .subscribe(result -> complete(future, result),
                                  t -> future.fail(BlueprintExceptionConverter.from(t)));
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
    public String configFile() { return "sql.json"; }

    private void complete(Future<Void> future, EventMessage result) {
        logger.info("{} SQL verticle {}", result.getAction(), result.toJson());
        if (result.isSuccess()) {
            logger.info("DATABASE IS READY TO USE");
        }
        future.complete();
    }

    private Single<EventMessage> createSchemaThenData(Configuration jooqConfig) {
        final String k = getSharedKey();
        final T handler = ((AbstractEntityHandler) getContext().createHandler(jooqConfig, vertx)).registerSharedKey(k);
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
            t = new BlueprintException(error.getCode(), error.getMessage());
        }
        if (result.getAction() == EventAction.INIT) {
            throw new InitializerError("Failed to startup SQL component", t);
        } else {
            throw new MigrationError("Failed to startup SQL component", t);
        }
    }

}
