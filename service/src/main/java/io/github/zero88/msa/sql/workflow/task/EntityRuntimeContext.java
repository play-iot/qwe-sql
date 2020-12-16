package io.github.zero88.msa.sql.workflow.task;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.msa.bp.dto.msg.RequestData;
import io.github.zero88.msa.bp.event.EventAction;
import io.github.zero88.msa.sql.EntityMetadata;
import io.github.zero88.msa.workflow.TaskExecutionContext;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder(builderClassName = "Builder")
public final class EntityRuntimeContext<P extends VertxPojo> implements TaskExecutionContext<P> {

    @NonNull
    private final RequestData originReqData;
    @NonNull
    private final EventAction originReqAction;
    @NonNull
    private final EntityMetadata metadata;
    private final P data;
    private final Throwable throwable;

}
