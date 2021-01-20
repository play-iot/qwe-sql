package io.github.zero88.qwe.sql.workflow;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.workflow.TaskExecuter;
import io.github.zero88.qwe.sql.pojos.DMLPojo;
import io.github.zero88.qwe.sql.validation.OperationValidator;
import io.github.zero88.qwe.sql.workflow.step.DeletionStep;
import io.github.zero88.qwe.sql.workflow.task.EntityTask.EntityPurgeTask;
import io.reactivex.Single;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Accessors(fluent = true)
public final class DefaultDeletionWorkflow extends DefaultDMLWorkflow<DeletionStep> {

    private final boolean supportForceDeletion;

    @Override
    protected @NonNull OperationValidator afterValidation() {
        return super.afterValidation().andThen(OperationValidator.create(this::purgeTask));
    }

    private Single<VertxPojo> purgeTask(@NonNull RequestData reqData, @NonNull VertxPojo pojo) {
        return TaskExecuter.execute(EntityPurgeTask.create(sqlStep().queryExecutor(), supportForceDeletion),
                                    initSuccessData(reqData, pojo)).map(DMLPojo::request).toSingle(pojo);
    }

}
