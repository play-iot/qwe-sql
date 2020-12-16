package io.github.zero88.msa.sql.workflow.task;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import io.github.zero88.msa.bp.transport.Transporter;
import io.github.zero88.msa.workflow.ProxyTask;

public interface ProxyEntityTask<DC extends EntityDefinitionContext, P extends VertxPojo, R, T extends Transporter>
    extends EntityTask<DC, P, R>, ProxyTask<DC, EntityRuntimeContext<P>, R, T> {

}
