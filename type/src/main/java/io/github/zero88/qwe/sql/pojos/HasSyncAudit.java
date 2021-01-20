package io.github.zero88.qwe.sql.pojos;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import io.github.zero88.qwe.sql.type.SyncAudit;

public interface HasSyncAudit extends VertxPojo {

    SyncAudit getSyncAudit();

    <T extends HasSyncAudit> T setSyncAudit(SyncAudit value);

}
