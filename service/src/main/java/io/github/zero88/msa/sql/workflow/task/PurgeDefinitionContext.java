package io.github.zero88.msa.sql.workflow.task;

import io.github.zero88.msa.sql.handler.EntityHandler;
import io.github.zero88.msa.sql.query.EntityQueryExecutor;
import io.github.zero88.msa.sql.service.cache.EntityServiceIndex;

import lombok.NonNull;

public interface PurgeDefinitionContext extends EntityDefinitionContext {

    static PurgeDefinitionContext create(@NonNull EntityQueryExecutor queryExecutor) {
        return () -> queryExecutor;
    }

    @NonNull EntityQueryExecutor queryExecutor();

    @Override
    default @NonNull EntityHandler entityHandler() {
        return queryExecutor().entityHandler();
    }

    @NonNull
    default EntityServiceIndex entityServiceIndex() {
        return entityHandler().sharedData(EntityServiceIndex.DATA_KEY);
    }

}
