package io.github.zero88.msa.sql.service.decorator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.zero88.msa.bp.dto.msg.RequestData;
import io.github.zero88.msa.sql.EntityMetadata;
import io.github.zero88.msa.sql.marker.ManyToManyMarker;

import lombok.NonNull;

public interface ManyToManyRequestDecorator extends HasReferenceRequestDecorator {

    ManyToManyMarker marker();

    @Override
    @NonNull
    default RequestData onCreatingOneResource(@NonNull RequestData requestData) {
        return recomputeRequestData(requestData, HasReferenceRequestDecorator.convertKey(requestData, context(),
                                                                                         marker().references()));
    }

    @Override
    @NonNull
    default RequestData onModifyingOneResource(@NonNull RequestData requestData) {
        final List<EntityMetadata> list = Stream.concat(marker().references().stream(), Stream.of(marker().resource()))
                                                .collect(Collectors.toList());
        return recomputeRequestData(requestData,
                                    HasReferenceRequestDecorator.convertKey(requestData, marker().context(), list));
    }

    @Override
    @NonNull
    default RequestData onDeletingOneResource(@NonNull RequestData requestData) {
        return onModifyingOneResource(requestData);
    }

    @Override
    @NonNull
    default RequestData onReadingManyResource(@NonNull RequestData requestData) {
        return onCreatingOneResource(requestData);
    }

    @Override
    default @NonNull RequestData onReadingOneResource(@NonNull RequestData requestData) {
        return onModifyingOneResource(requestData);
    }

}
