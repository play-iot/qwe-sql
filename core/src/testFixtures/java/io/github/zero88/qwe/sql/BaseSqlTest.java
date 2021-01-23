package io.github.zero88.qwe.sql;

import java.util.Objects;
import java.util.UUID;

import org.jooq.Catalog;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.LoggerFactory;

import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.TestHelper;
import io.github.zero88.qwe.TestHelper.VertxHelper;
import io.github.zero88.qwe.component.ComponentSharedDataHelper;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.sql.schema.SchemaHandler;
import io.github.zero88.qwe.sql.schema.SchemaInitializer;
import io.github.zero88.qwe.sql.schema.SchemaMigrator;
import io.github.zero88.qwe.utils.Configs;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseSqlTest {

    protected final String sharedKey = getClass().getName();
    protected Vertx vertx;
    private DeploymentOptions options;
    private String deployId;

    @BeforeClass
    public static void beforeSuite() {
        TestHelper.setup();
        ((Logger) LoggerFactory.getLogger("org.jooq")).setLevel(Level.DEBUG);
        ((Logger) LoggerFactory.getLogger("com.zaxxer.hikari")).setLevel(Level.DEBUG);
    }

    public static SchemaHandler createSchemaHandler(Table table, SchemaInitializer initializer,
                                                    SchemaMigrator migrator) {
        return new SchemaHandler() {
            @Override
            public @NonNull Table table() {
                return table;
            }

            @Override
            public @NonNull SchemaInitializer initializer() {
                return initializer;
            }

            @Override
            public @NonNull SchemaMigrator migrator() {
                return migrator;
            }
        };
    }

    @Before
    public final void before(TestContext context) {
        SqlConfig sqlConfig = IConfig.from(Configs.loadJsonConfig("sql.json"), SqlConfig.class);
        sqlConfig.setDialect(getDialect());
        sqlConfig.getHikariConfig().setJdbcUrl(getJdbcUrl());
        vertx = Vertx.vertx().exceptionHandler(context.exceptionHandler());
        options = new DeploymentOptions().setConfig(sqlConfig.toJson());
        setup(context);
    }

    @After
    public final void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @NonNull
    protected String getJdbcUrl() {
        return "jdbc:h2:mem:dbh2mem-" + UUID.randomUUID().toString();
    }

    protected SQLDialect getDialect()         { return SQLDialect.H2; }

    protected void setup(TestContext context) { }

    protected EventbusClient controller() {
        return EventbusClient.create(vertx, sharedKey);
    }

    protected void stopSQL(TestContext context) {
        System.out.println("Stop deployId: " + deployId);
        if (Objects.nonNull(deployId)) {
            vertx.undeploy(deployId, context.asyncAssertSuccess());
        }
    }

    protected <T extends AbstractEntityHandler> T startSQL(TestContext context, Catalog catalog,
                                                           Class<T> handlerClass) {
        SqlVerticle<T> v = VertxHelper.deploy(vertx, context, options, create(handlerClass),
                                              TestHelper.TEST_TIMEOUT_SEC * 2);
        deployId = v.deploymentID();
        return v.getContext().getEntityHandler();
    }

    protected <T extends AbstractEntityHandler> void startSQLFailed(TestContext context, Catalog catalog,
                                                                    Class<T> handlerClass,
                                                                    Handler<Throwable> consumer) {
        VertxHelper.deployFailed(vertx, context, options, create(handlerClass), consumer);
    }

    private <T extends AbstractEntityHandler> SqlVerticle create(Class<T> handlerClass) {
        return new SqlProvider<>(handlerClass).provide(ComponentSharedDataHelper.create(vertx, SqlVerticle.class));
    }

}
