package io.github.zero88.qwe.sql.schema;

import io.github.zero88.qwe.sql.handler.EntityHandler;
import io.reactivex.Single;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import io.github.zero88.qwe.event.EventMessage;

import lombok.NonNull;

/**
 * Represents for schema executor.
 *
 * @since 1.0.0
 */
public interface SchemaExecutor {

    /**
     * Get logger.
     *
     * @return the logger
     * @since 1.0.0
     */
    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Execute task.
     *
     * @param entityHandler entity handler
     * @return the result in single
     * @see EntityHandler
     * @since 1.0.0
     */
    Single<EventMessage> execute(@NonNull EntityHandler entityHandler);

}
