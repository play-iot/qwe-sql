package io.github.zero88.msa.sql.pojos;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import io.github.zero88.msa.sql.type.TimeAudit;

public interface HasTimeAudit extends VertxPojo {

    TimeAudit getTimeAudit();

    <T extends HasTimeAudit> T setTimeAudit(TimeAudit value);

}
