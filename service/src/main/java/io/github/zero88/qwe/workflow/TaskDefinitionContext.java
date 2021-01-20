package io.github.zero88.qwe.workflow;

import io.github.zero88.qwe.component.SharedDataDelegate;
import io.vertx.core.Vertx;

import lombok.NonNull;

/**
 * Represents {@code Task definition context}.
 *
 * @see SharedDataDelegate
 * @since 1.0.0
 */
public interface TaskDefinitionContext extends TaskContext, SharedDataDelegate<TaskDefinitionContext> {

    /**
     * Vertx
     *
     * @return vertx instance
     * @since 1.0.0
     */
    @NonNull Vertx vertx();

    /**
     * Define {@code task} will be executed in {@code another worker thread} or {@code same thread} with current
     * thread.
     *
     * @return {@code true} if {@code task} will be executed in {@code another worker}
     * @since 1.0.0
     */
    boolean isConcurrent();

}
