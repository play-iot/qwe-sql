package io.github.zero88.msa.sql.service;

import io.github.zero88.msa.sql.EntityMetadata;

import lombok.NonNull;

public interface EntityApiService {

    @NonNull String prefixServiceName();

    @NonNull
    default String lookupApiName(@NonNull EntityMetadata metadata) {
        return prefixServiceName() + "." + metadata.modelClass().getSimpleName();
    }

}
