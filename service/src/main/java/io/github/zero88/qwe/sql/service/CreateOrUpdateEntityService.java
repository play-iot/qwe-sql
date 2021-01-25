package io.github.zero88.qwe.sql.service;

import java.util.Collection;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.qwe.micro.http.ActionMethodMapping;
import io.github.zero88.qwe.sql.EntityMetadata;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.exceptions.NotFoundException;

import lombok.NonNull;

/**
 * Represents for an {@code entity service} that supports {@code create_or_update} event
 *
 * @param <P> Type of {@code VertxPojo}
 * @param <M> Type of {@code EntityMetadata}
 * @since 1.0.0
 */
public interface CreateOrUpdateEntityService<P extends VertxPojo, M extends EntityMetadata>
    extends EntityService<P, M> {

    @Override
    default @NonNull Collection<EventAction> getAvailableEvents() {
        return ActionMethodMapping.CRD_MAP.get().keySet();
    }

    /**
     * Create or update entity.
     *
     * @param requestData the request data
     * @return json result
     * @since 1.0.0
     */
    @EventContractor(action = "CREATE_OR_UPDATE", returnType = Single.class)
    default Single<JsonObject> createOrUpdate(@NonNull RequestData requestData) {
        return patch(requestData).onErrorResumeNext(t -> {
            if (t instanceof NotFoundException) {
                return create(requestData);
            }
            return Single.error(t);
        });
    }

}
