package io.github.zero88.msa.sql.service.decorator;

import java.util.stream.Stream;

import io.github.zero88.msa.bp.dto.msg.RequestData;
import io.github.zero88.msa.sql.marker.GroupReferencingEntityMarker;
import io.vertx.core.json.JsonObject;

import lombok.NonNull;

public interface GroupRequestDecorator extends HasReferenceRequestDecorator {

    GroupReferencingEntityMarker marker();

    @Override
    default @NonNull RequestData onCreatingOneResource(@NonNull RequestData requestData) {
        return recomputeRequestData(requestData, HasReferenceRequestDecorator.convertKey(requestData, Stream.concat(
            marker().referencedEntities().stream(), marker().groupReferences().stream())));
    }

    @Override
    default @NonNull RequestData onModifyingOneResource(@NonNull RequestData requestData) {
        final JsonObject extra = HasReferenceRequestDecorator.convertKey(requestData, context(), context());
        final JsonObject groupExtra = HasReferenceRequestDecorator.convertKey(requestData, Stream.concat(
            marker().referencedEntities().stream(), marker().groupReferences().stream()));
        return recomputeRequestData(requestData, extra.mergeIn(groupExtra, true));
    }

}
