package io.github.zero88.qwe.sql.service;

import io.github.zero88.qwe.sql.CompositeMetadata;
import io.github.zero88.qwe.sql.EntityMetadata;
import io.github.zero88.qwe.sql.handler.EntityHandler;
import io.github.zero88.qwe.sql.marker.ManyToManyMarker;
import io.github.zero88.qwe.sql.pojos.CompositePojo;
import io.github.zero88.qwe.sql.service.transformer.ManyToManyEntityTransformer;
import io.github.zero88.qwe.sql.validation.CompositeValidation;
import io.github.zero88.qwe.sql.validation.OperationValidator;
import io.reactivex.Single;

import io.github.zero88.qwe.sql.service.decorator.ManyToManyRequestDecorator;
import io.github.zero88.qwe.sql.query.ComplexQueryExecutor;

import lombok.NonNull;

/**
 * Abstract service to implement {@code CRUD} listeners for the {@code database entity} has a {@code many-to-many}
 * relationship.
 *
 * @param <P> Type of {@code CompositePojo}
 * @param <M> Type of {@code CompositeMetadata}
 * @see ManyToManyEntityService
 * @see ManyToManyRequestDecorator
 * @see ManyToManyEntityTransformer
 * @since 1.0.0
 */
public abstract class AbstractManyToManyEntityService<P extends CompositePojo, M extends CompositeMetadata>
    extends AbstractEntityService<P, M>
    implements ManyToManyEntityService<P, M>, ManyToManyRequestDecorator, ManyToManyEntityTransformer {

    /**
     * Instantiates a new Abstract many to many entity service.
     *
     * @param entityHandler the entity handler
     * @since 1.0.0
     */
    public AbstractManyToManyEntityService(@NonNull EntityHandler entityHandler) {
        super(entityHandler);
    }

    @Override
    public abstract M context();

    @Override
    public @NonNull ComplexQueryExecutor<P> queryExecutor() {
        return ManyToManyEntityService.super.queryExecutor();
    }

    @Override
    public @NonNull CompositeValidation validation() { return this.context(); }

    @Override
    public @NonNull ManyToManyEntityTransformer transformer() { return this; }

    @Override
    public @NonNull EntityMetadata resourceMetadata() {
        return resource();
    }

    @Override
    protected OperationValidator initCreationValidator() {
        return OperationValidator.create((req, pojo) -> Single.just(context().onCreating(req)));
    }

    @Override
    public ManyToManyMarker marker() { return this; }

}
