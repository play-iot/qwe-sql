package io.github.zero88.qwe.sql.workflow.step;

import java.util.Objects;
import java.util.function.BiConsumer;

import org.jooq.Configuration;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.qwe.sql.pojos.DMLPojo;
import io.reactivex.Single;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.sql.validation.OperationValidator;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Accessors(fluent = true)
@SuppressWarnings("unchecked")
public final class DeletionStep extends AbstractSQLStep implements DMLStep {

    @Setter
    private BiConsumer<EventAction, DMLPojo> onSuccess;

    @Override
    public Single<DMLPojo> execute(@NonNull RequestData requestData, @NonNull OperationValidator validator,
                                   Configuration configuration) {
        final Single<DMLPojo> result = queryExecutor().runtimeConfiguration(configuration)
                                                      .deleteOneByKey(requestData, validator)
                                                      .map(p -> DMLPojo.builder().dbEntity((VertxPojo) p).build());
        if (Objects.nonNull(onSuccess)) {
            return result.doOnSuccess(keyPojo -> onSuccess.accept(action(), keyPojo));
        }
        return result;
    }

}
