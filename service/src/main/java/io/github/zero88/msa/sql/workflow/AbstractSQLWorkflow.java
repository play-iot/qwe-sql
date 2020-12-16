package io.github.zero88.msa.sql.workflow;

import java.util.function.Function;

import org.jooq.Configuration;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.msa.bp.dto.msg.RequestData;
import io.github.zero88.msa.bp.event.EventAction;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import io.github.zero88.msa.sql.EntityMetadata;
import io.github.zero88.msa.sql.validation.OperationValidator;
import io.github.zero88.msa.sql.workflow.task.EntityRuntimeContext;
import io.github.zero88.msa.sql.workflow.task.EntityTaskManager;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Accessors(fluent = true)
abstract class AbstractSQLWorkflow implements SQLWorkflow {

    @NonNull
    private final EventAction action;
    @NonNull
    private final EntityMetadata metadata;
    @NonNull
    private final Function<RequestData, RequestData> normalize;
    @NonNull
    private final OperationValidator validator;
    @NonNull
    private final EntityTaskManager taskManager;

    @NonNull
    @Override
    public final Single<JsonObject> run(@NonNull RequestData requestData) {
        return run(requestData, null);
    }

    @NonNull
    protected abstract Single<JsonObject> run(@NonNull RequestData requestData, Configuration runtimeConfig);

    @NonNull
    protected OperationValidator afterValidation() {
        return OperationValidator.create((req, pojo) -> taskManager().preBlockingExecuter()
                                                                     .execute(initSuccessData(req, pojo))
                                                                     .switchIfEmpty(Single.just(pojo)));
    }

    @NonNull
    protected EntityRuntimeContext<VertxPojo> initSuccessData(@NonNull RequestData reqData, @NonNull VertxPojo pojo) {
        return taskData(reqData, pojo, null);
    }

    @NonNull
    protected EntityRuntimeContext<VertxPojo> initErrorData(@NonNull RequestData reqData, @NonNull Throwable err) {
        return taskData(reqData, null, err);
    }

    @NonNull
    protected EntityRuntimeContext<VertxPojo> taskData(@NonNull RequestData reqData, VertxPojo pojo, Throwable t) {
        return EntityRuntimeContext.builder()
                                   .originReqData(reqData)
                                   .originReqAction(action())
                                   .metadata(metadata())
                                   .data(pojo)
                                   .throwable(t)
                                   .build();
    }

}
