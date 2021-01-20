package io.github.zero88.qwe.sql.service;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.qwe.sql.handler.EntityHandler;
import io.github.zero88.qwe.sql.EntityMetadata;
import io.github.zero88.qwe.sql.service.decorator.HasReferenceRequestDecorator;
import io.github.zero88.qwe.sql.marker.TransitiveReferenceMarker;
import io.github.zero88.qwe.sql.query.TransitiveReferenceQueryExecutor;

import lombok.NonNull;

/**
 * Abstract service to implement {@code CRUD} listeners for the {@code many-to-one entity} with {@code transitive
 * resource}.
 *
 * @param <P> Type of {@code VertxPojo}
 * @param <M> Type of {@code EntityMetadata}
 * @see TransitiveReferenceMarker
 * @see AbstractReferencingEntityService
 * @since 1.0.0
 */
public abstract class AbstractTransitiveEntityService<P extends VertxPojo, M extends EntityMetadata>
    extends AbstractReferencingEntityService<P, M> implements HasReferenceRequestDecorator, TransitiveReferenceMarker {

    /**
     * Instantiates a new Abstract one to many entity service.
     *
     * @param entityHandler the entity handler
     * @since 1.0.0
     */
    public AbstractTransitiveEntityService(@NonNull EntityHandler entityHandler) {
        super(entityHandler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NonNull TransitiveReferenceQueryExecutor<P> queryExecutor() {
        return TransitiveReferenceQueryExecutor.create(entityHandler(), context(), this);
    }

    @Override
    public TransitiveReferenceMarker marker() {
        return this;
    }

}
