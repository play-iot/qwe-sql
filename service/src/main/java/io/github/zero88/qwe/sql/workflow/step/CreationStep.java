package io.github.zero88.qwe.sql.workflow.step;

import java.util.Objects;
import java.util.function.BiConsumer;

import org.jooq.Configuration;

import io.github.zero88.qwe.sql.pojos.DMLPojo;
import io.github.zero88.qwe.sql.validation.OperationValidator;
import io.github.zero88.qwe.sql.workflow.step.DMLStep.CreateOrUpdateStep;
import io.reactivex.Single;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Accessors(fluent = true)
@SuperBuilder
@SuppressWarnings("unchecked")
public final class CreationStep extends AbstractSQLStep implements CreateOrUpdateStep {

    @Setter
    private BiConsumer<EventAction, DMLPojo> onSuccess;

    @Override
    public Single<DMLPojo> execute(@NonNull RequestData requestData, @NonNull OperationValidator validator,
                                   Configuration configuration) {
        final Single<DMLPojo> result = queryExecutor().runtimeConfiguration(configuration)
                                                      .insertReturningPrimary(requestData, validator)
                                                      .flatMap(pojo -> lookup((DMLPojo) pojo));
        if (Objects.nonNull(onSuccess)) {
            return result.doOnSuccess(keyPojo -> onSuccess.accept(action(), keyPojo));
        }
        return result;
    }

}
