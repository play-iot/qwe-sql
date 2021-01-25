package io.github.zero88.qwe.sql;

import java.util.Objects;
import java.util.UUID;

import org.jooq.SQLDialect;
import org.jooq.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.slf4j.LoggerFactory;

import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.TestHelper;
import io.github.zero88.qwe.component.ComponentTestHelper;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.sql.schema.SchemaHandler;
import io.github.zero88.qwe.sql.schema.SchemaInitializer;
import io.github.zero88.qwe.sql.schema.SchemaMigrator;
import io.github.zero88.qwe.utils.Configs;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseSqlTest {

    protected Vertx vertx;
    private SqlConfig config;
    private String deployId;
    private EventbusClient eventbus;
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

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
        config = IConfig.from(Configs.loadJsonConfig("sql.json"), SqlConfig.class);
        config.setDialect(getDialect());
        config.getHikariConfig().setJdbcUrl(getJdbcUrl());
        vertx = Vertx.vertx().exceptionHandler(context.exceptionHandler());
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

    protected EventbusClient eventbus() {
        return eventbus;
    }

    protected void stopSQL(TestContext context) {
        System.out.println("Stop deployId: " + deployId);
        if (Objects.nonNull(deployId)) {
            vertx.undeploy(deployId, context.asyncAssertSuccess());
        }
    }

    protected <T extends AbstractEntityHandler> T startSQL(TestContext context, Class<T> handlerClass) {
        SqlVerticle<T> v = ComponentTestHelper.deploy(vertx, context, config.toJson(), new SqlProvider<>(handlerClass),
                                                      folder.getRoot().toPath());
        deployId = v.getContext().deployId();
        eventbus = EventbusClient.create(v.sharedData());
        return v.getContext().getEntityHandler();
    }

    protected <T extends AbstractEntityHandler> void startSQLFailed(TestContext context, Class<T> handlerClass,
                                                                    Handler<Throwable> consumer) {
        ComponentTestHelper.deployFailed(vertx, context, config.toJson(), new SqlProvider<>(handlerClass), consumer);
    }

}
