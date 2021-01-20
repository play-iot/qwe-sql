package io.github.zero88.qwe.sql.workflow.step;

import java.util.Objects;

import org.jooq.Configuration;

import io.github.zero88.qwe.sql.pojos.DMLPojo;
import io.github.zero88.qwe.sql.validation.OperationValidator;
import io.github.zero88.qwe.sql.workflow.step.DMLStep.CreateOrUpdateStep;
import io.github.zero88.utils.Functions.TripleConsumer;
import io.reactivex.Single;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Accessors(fluent = true)
@SuppressWarnings("unchecked")
public final class ModificationStep extends AbstractSQLStep implements CreateOrUpdateStep {

    @Setter
    private TripleConsumer<RequestData, EventAction, DMLPojo> onSuccess;

    @Override
    public Single<DMLPojo> execute(@NonNull RequestData requestData, @NonNull OperationValidator validator,
                                   Configuration configuration) {
        final Single<DMLPojo> result = queryExecutor().runtimeConfiguration(configuration)
                                                      .modifyReturningPrimary(requestData, validator)
                                                      .flatMap(dmlPojo -> lookup((DMLPojo) dmlPojo));
        if (Objects.nonNull(onSuccess)) {
            return result.doOnSuccess(keyPojo -> onSuccess.accept(requestData, action(), keyPojo));
        }
        return result;
    }

}
