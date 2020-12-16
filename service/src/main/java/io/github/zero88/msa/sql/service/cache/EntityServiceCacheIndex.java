package io.github.zero88.msa.sql.service.cache;

import io.github.zero88.msa.sql.EntityMetadata;

import lombok.NonNull;

public interface EntityServiceCacheIndex extends EntityServiceIndex {

    static EntityServiceCacheIndex create() {
        return new DefaultEntityServiceCache();
    }

    EntityServiceCacheIndex add(@NonNull EntityMetadata metadata, @NonNull String serviceAddress);

}
