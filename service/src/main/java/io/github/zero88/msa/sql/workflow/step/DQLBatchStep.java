package io.github.zero88.msa.sql.workflow.step;

import java.util.function.BiFunction;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.github.zero88.msa.bp.dto.msg.RequestData;
import io.github.zero88.msa.sql.validation.OperationValidator;

import lombok.NonNull;

public interface DQLBatchStep extends SQLBatchStep {

    @NonNull BiFunction<VertxPojo, RequestData, Single<JsonObject>> onEach();

    /**
     * Do {@code SQL Query} based on given {@code request data} and {@code validator}.
     *
     * @param reqData   the req data
     * @param validator the validator
     * @return result in Single
     * @see RequestData
     * @see OperationValidator
     * @since 1.0.0
     */
    Single<JsonArray> query(@NonNull RequestData reqData, @NonNull OperationValidator validator);

}
