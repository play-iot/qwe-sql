package io.github.zero88.qwe.sql;

import java.nio.file.Path;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.sql.handler.EntityHandler;
import io.vertx.core.Vertx;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * Represents for Abstract entity handler.
 *
 * @since 1.0.0
 */
public abstract class AbstractEntityHandler implements EntityHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @NonNull
    final Configuration jooqConfig;
    @Getter
    @Accessors(fluent = true)
    private final SharedDataLocalProxy sharedData;

    /**
     * Instantiates a new Abstract entity handler.
     *
     * @param jooqConfig the jooq config
     * @param sharedData the shared data proxy
     * @since 1.0.0
     */
    public AbstractEntityHandler(@NonNull Configuration jooqConfig, @NonNull SharedDataLocalProxy sharedData) {
        this.jooqConfig = jooqConfig;
        this.sharedData = sharedData;
    }

    @Override
    public Vertx vertx() {
        return sharedData().getVertx();
    }

    @Override
    public EventbusClient eventClient() {
        return EventbusClient.create(sharedData());
    }

    @Override
    public Path dataDir() {
        return sharedData().getData(SharedDataLocalProxy.APP_DATADIR);
    }

    @Override
    public DSLContext dsl() {
        return jooqConfig.dsl();
    }

}
