package io.github.zero88.msa.sql.service;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import io.github.zero88.msa.sql.EntityMetadata;
import io.github.zero88.msa.sql.query.SimpleQueryExecutor;

import lombok.NonNull;

interface SimpleEntityService<P extends VertxPojo, M extends EntityMetadata> extends EntityService<P, M> {

    @Override
    @NonNull SimpleQueryExecutor<P> queryExecutor();

}
