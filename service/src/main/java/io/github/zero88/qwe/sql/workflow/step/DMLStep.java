package io.github.zero88.qwe.sql.workflow.step;

import org.jooq.Configuration;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.utils.ExecutorHelpers;
import io.github.zero88.qwe.sql.pojos.DMLPojo;
import io.github.zero88.qwe.sql.validation.OperationValidator;
import io.reactivex.Single;

import lombok.NonNull;

/**
 * Represents a {@code DML} step for single resource
 *
 * @since 1.0.0
 */
public interface DMLStep extends SQLStep {

    /**
     * Execute {@code SQL manipulate command} based on given {@code request data} and {@code validator}.
     *
     * @param requestData   the sql runtime context
     * @param validator     the validator
     * @param configuration the configuration
     * @return DML pojo in Single
     * @see OperationValidator
     * @see DMLPojo
     * @since 1.0.0
     */
    Single<DMLPojo> execute(@NonNull RequestData requestData, @NonNull OperationValidator validator,
                            Configuration configuration);

    /**
     * Represents a {@code create} or {@code update} step
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    interface CreateOrUpdateStep extends DMLStep {

        /**
         * Lookup created or modified entity by primary key
         *
         * @param dmlPojo Request pojo with primary key
         * @return wrapper pojo
         * @see DMLPojo
         * @since 1.0.0
         */
        default Single<DMLPojo> lookup(@NonNull DMLPojo dmlPojo) {
            return ExecutorHelpers.blocking(queryExecutor().entityHandler().vertx(),
                                            queryExecutor().lookupByPrimaryKey(dmlPojo.primaryKey())
                                                           .map(p -> DMLPojo.builder()
                                                                            .request(dmlPojo.request())
                                                                            .primaryKey(dmlPojo.primaryKey())
                                                                            .dbEntity((VertxPojo) p)
                                                                            .build()));
        }

    }

}
