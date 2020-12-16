package io.github.zero88.msa.sql.service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.msa.bp.dto.msg.RequestData;
import io.github.zero88.msa.bp.event.EventAction;
import io.github.zero88.msa.bp.event.EventContractor;
import io.github.zero88.msa.bp.micro.metadata.ActionMethodMapping;
import io.github.zero88.msa.sql.EntityMetadata;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import lombok.NonNull;

/**
 * Represents for {@code entity service} in batch mode.
 *
 * @param <P> Type of {@code VertxPojo}
 * @param <M> Type of {@code EntityMetadata}
 * @see io.github.zero88.msa.sql.service.EntityService
 * @since 1.0.0
 */
public interface BatchEntityService<P extends VertxPojo, M extends EntityMetadata> extends EntityService<P, M> {

    @NonNull
    default Collection<EventAction> getAvailableEvents() {
        return Stream.of(EntityService.super.getAvailableEvents(), ActionMethodMapping.BATCH_DML_MAP.get().keySet())
                     .flatMap(Collection::stream)
                     .collect(Collectors.toSet());
    }

    /**
     * Defines listener for updating existing resources in batch
     *
     * @param requestData Request data
     * @return json object that includes status message
     * @see EventAction#BATCH_CREATE
     * @since 1.0.0
     */
    @EventContractor(action = "BATCH_CREATE", returnType = Single.class)
    Single<JsonObject> batchCreate(@NonNull RequestData requestData);

    /**
     * Defines listener for updating existing resources in batch
     *
     * @param requestData Request data
     * @return json object that includes status message
     * @see EventAction#BATCH_UPDATE
     * @since 1.0.0
     */
    @EventContractor(action = "BATCH_UPDATE", returnType = Single.class)
    Single<JsonObject> batchUpdate(@NonNull RequestData requestData);

    /**
     * Defines listener for patching existing resources in batch
     *
     * @param requestData Request data
     * @return json object that includes status message
     * @see EventAction#BATCH_PATCH
     * @since 1.0.0
     */
    @EventContractor(action = "BATCH_PATCH", returnType = Single.class)
    Single<JsonObject> batchPatch(@NonNull RequestData requestData);

    /**
     * Defines listener for deleting existing resources in batch
     *
     * @param requestData Request data
     * @return json object that includes status message
     * @see EventAction#BATCH_DELETE
     * @since 1.0.0
     */
    @EventContractor(action = "BATCH_DELETE", returnType = Single.class)
    Single<JsonObject> batchDelete(@NonNull RequestData requestData);

}
