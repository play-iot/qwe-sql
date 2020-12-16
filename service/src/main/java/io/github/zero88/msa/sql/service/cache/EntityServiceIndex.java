package io.github.zero88.msa.sql.service.cache;

import io.github.zero88.msa.sql.EntityMetadata;

import lombok.NonNull;

public interface EntityServiceIndex {

    String DATA_KEY = "ENTITY_SERVICE_INDEX";

    @NonNull String lookupApiAddress(@NonNull EntityMetadata metadata);

}
