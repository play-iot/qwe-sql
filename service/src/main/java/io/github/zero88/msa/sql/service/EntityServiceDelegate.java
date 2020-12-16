package io.github.zero88.msa.sql.service;

import java.util.Collection;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.reactivex.Single;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zero88.msa.bp.dto.msg.RequestData;
import io.github.zero88.msa.bp.event.EventAction;
import io.github.zero88.msa.bp.event.EventMessage;
import io.github.zero88.msa.sql.handler.EntityHandler;
import io.github.zero88.msa.sql.EntityMetadata;
import io.github.zero88.msa.sql.service.transformer.EntityTransformer;
import io.github.zero88.msa.sql.service.decorator.RequestDecorator;
import io.github.zero88.msa.sql.query.EntityQueryExecutor;
import io.github.zero88.msa.sql.validation.EntityValidation;
import io.github.zero88.msa.sql.workflow.task.EntityTaskManager;

import lombok.NonNull;

/**
 * The interface Entity service delegate.
 *
 * @param <P> Type of {@code VertxPojo}
 * @param <M> Type of {@code Entity Metadata}
 * @param <S> Type of {@code Entity Service}
 * @since 1.0.0
 */
public interface EntityServiceDelegate<P extends VertxPojo, M extends EntityMetadata, S extends io.github.zero88.msa.sql.service.EntityService<P, M>>
    extends io.github.zero88.msa.sql.service.EntityService<P, M> {

    /**
     * Unwrap Entity service.
     *
     * @return the service
     * @since 1.0.0
     */
    S unwrap();

    @Override
    default @NonNull EntityHandler entityHandler() {
        return unwrap().entityHandler();
    }

    @Override
    default M context() {
        return unwrap().context();
    }

    @Override
    default @NonNull Collection<EventAction> getAvailableEvents() {
        return unwrap().getAvailableEvents();
    }

    @Override
    default @NonNull EntityQueryExecutor<P> queryExecutor() {
        return unwrap().queryExecutor();
    }

    @Override
    default @NonNull RequestDecorator requestDecorator() {
        return unwrap().requestDecorator();
    }

    @Override
    default EntityValidation validation() {
        return unwrap().validation();
    }

    @Override
    default @NonNull EntityTransformer transformer() {
        return unwrap().transformer();
    }

    @Override
    default @NonNull EntityTaskManager taskManager() {
        return unwrap().taskManager();
    }

    @Override
    default boolean supportForceDeletion() {
        return unwrap().supportForceDeletion();
    }

    @Override
    default Single<JsonObject> list(RequestData requestData) {
        return unwrap().list(requestData);
    }

    @Override
    default Single<JsonObject> get(RequestData requestData) {
        return unwrap().get(requestData);
    }

    @Override
    default Single<JsonObject> create(RequestData requestData) {
        return unwrap().create(requestData);
    }

    @Override
    default Single<JsonObject> update(RequestData requestData) {
        return unwrap().update(requestData);
    }

    @Override
    default Single<JsonObject> patch(RequestData requestData) {
        return unwrap().patch(requestData);
    }

    @Override
    default Single<JsonObject> delete(RequestData requestData) {
        return unwrap().delete(requestData);
    }

    @Override
    default Logger logger() {
        return unwrap().logger();
    }

    @Override
    default ObjectMapper mapper() {
        return unwrap().mapper();
    }

    @Override
    default String fallback() {
        return unwrap().fallback();
    }

    @Override
    default Single<EventMessage> apply(Message<Object> message) {
        return unwrap().apply(message);
    }

}
