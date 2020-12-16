package io.github.zero88.msa.sql.workflow;

import java.util.function.Function;

import io.github.zero88.msa.bp.dto.msg.RequestData;
import io.github.zero88.msa.bp.event.EventAction;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import io.github.zero88.msa.sql.EntityMetadata;
import io.github.zero88.msa.sql.transaction.JDBCRXTransactionExecutor;
import io.github.zero88.msa.sql.validation.OperationValidator;
import io.github.zero88.msa.sql.workflow.step.DMLStep;
import io.github.zero88.msa.sql.workflow.task.EntityTaskManager;

import lombok.Builder;
import lombok.NonNull;

@Builder
public final class DefaultDMLTransactionWorkflow implements DMLTransactionWorkflow {

    @NonNull
    private final DMLWorkflow workflow;

    @Override
    public @NonNull EventAction action() {
        return workflow.action();
    }

    @Override
    public @NonNull EntityMetadata metadata() {
        return workflow.metadata();
    }

    @Override
    public @NonNull Function<RequestData, RequestData> normalize() {
        return workflow.normalize();
    }

    @Override
    public @NonNull OperationValidator validator() {
        return workflow.validator();
    }

    @Override
    public @NonNull EntityTaskManager taskManager() {
        return workflow.taskManager();
    }

    @Override
    public @NonNull Single<JsonObject> run(@NonNull RequestData reqData) {
        return JDBCRXTransactionExecutor.create(sqlStep().queryExecutor().entityHandler().dsl())
                                        .transactionResult(c -> ((AbstractSQLWorkflow) workflow).run(reqData, c));
    }

    @Override
    public @NonNull DMLStep sqlStep() {
        return workflow.sqlStep();
    }

}
