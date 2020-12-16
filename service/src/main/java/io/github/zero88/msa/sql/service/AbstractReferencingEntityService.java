package io.github.zero88.msa.sql.service;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import io.github.zero88.msa.sql.handler.EntityHandler;
import io.github.zero88.msa.sql.EntityMetadata;
import io.github.zero88.msa.sql.service.decorator.HasReferenceRequestDecorator;
import io.github.zero88.msa.sql.service.transformer.ReferencingEntityTransformer;
import io.github.zero88.msa.sql.query.ReferencingQueryExecutor;
import io.github.zero88.msa.sql.marker.ReferencingEntityMarker;
import io.github.zero88.msa.sql.validation.OperationValidator;

import lombok.NonNull;

/**
 * Abstract service to implement {@code CRUD} listeners for the {@code database entity} has a {@code many-to-one}
 * relationship.
 *
 * @param <P> Type of {@code VertxPojo}
 * @param <M> Type of {@code EntityMetadata}
 * @see ReferencingEntityService
 * @see HasReferenceRequestDecorator
 * @see ReferencingEntityTransformer
 * @since 1.0.0
 */
public abstract class AbstractReferencingEntityService<P extends VertxPojo, M extends EntityMetadata>
    extends io.github.zero88.msa.sql.service.AbstractEntityService<P, M>
    implements ReferencingEntityService<P, M>, HasReferenceRequestDecorator, ReferencingEntityTransformer {

    /**
     * Instantiates a new Abstract one to many entity service.
     *
     * @param entityHandler the entity handler
     * @since 1.0.0
     */
    public AbstractReferencingEntityService(@NonNull EntityHandler entityHandler) {
        super(entityHandler);
    }

    @Override
    public @NonNull ReferencingQueryExecutor<P> queryExecutor() {
        return ReferencingEntityService.super.queryExecutor();
    }

    @Override
    public @NonNull ReferencingEntityTransformer transformer() {
        return this;
    }

    protected OperationValidator initCreationValidator() {
        return super.initCreationValidator().andThen(checkReferenceExisted());
    }

    @Override
    protected @NonNull OperationValidator initPatchValidator() {
        return super.initPatchValidator().andThen(checkReferenceExisted());
    }

    @Override
    protected @NonNull OperationValidator initUpdateValidator() {
        return super.initUpdateValidator().andThen(checkReferenceExisted());
    }

    @Override
    public ReferencingEntityMarker marker() {
        return this;
    }

    @NonNull
    protected OperationValidator checkReferenceExisted() {
        return OperationValidator.create((req, pojo) -> queryExecutor().checkReferenceExistence(req).map(b -> pojo));
    }

}
