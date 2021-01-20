package io.github.zero88.qwe.sql.workflow;

import org.jooq.Configuration;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.sql.workflow.step.DMLBatchStep;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Accessors(fluent = true)
public final class DefaultDMLBatchWorkflow extends AbstractSQLWorkflow implements DMLBatchWorkflow {

    @NonNull
    private final DMLBatchStep sqlStep;
    private final boolean continueOnError;

    @Override
    protected @NonNull Single<JsonObject> run(@NonNull RequestData requestData, Configuration runtimeConfig) {
        return Single.error(new UnsupportedOperationException("Not yet implemented"));
    }

}
