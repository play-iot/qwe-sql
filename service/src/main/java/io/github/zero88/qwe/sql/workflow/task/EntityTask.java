package io.github.zero88.qwe.sql.workflow.task;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.sql.pojos.DMLPojo;
import io.github.zero88.qwe.sql.query.EntityQueryExecutor;
import io.github.zero88.qwe.workflow.Task;

import lombok.NonNull;

/**
 * Represents Entity Task
 *
 * @param <DC> Type of {@code EntityTaskContext}
 * @param <P>  Type of {@code VertxPojo}
 * @param <R>  Type of {@code Result}
 * @see Task
 * @see EntityDefinitionContext
 * @see EntityRuntimeContext
 * @since 1.0.0
 */
public interface EntityTask<DC extends EntityDefinitionContext, P extends VertxPojo, R>
    extends Task<DC, EntityRuntimeContext<P>, R> {

    interface EntityNormalTask<DC extends EntityDefinitionContext, P extends VertxPojo, R>
        extends EntityTask<DC, P, R> {

    }


    interface EntityPurgeTask<DC extends PurgeDefinitionContext, P extends VertxPojo, R>
        extends EntityTask<DC, P, R>, ProxyEntityTask<DC, P, R, EventbusClient> {

        static <P extends VertxPojo> EntityPurgeTask<PurgeDefinitionContext, P, DMLPojo> create(
            @NonNull EntityQueryExecutor queryExecutor, boolean supportForceDeletion) {
            return new DefaultEntityPurgeTask<>(PurgeDefinitionContext.create(queryExecutor), supportForceDeletion);
        }

        @Override
        default EventbusClient transporter() {
            return definitionContext().entityHandler().eventClient();
        }

    }

}
