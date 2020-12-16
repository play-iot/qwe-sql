package io.github.zero88.msa.sql.service.cache;

import org.jooq.Configuration;

import io.github.zero88.msa.bp.cache.AbstractLocalCache;
import io.github.zero88.msa.bp.dto.msg.DataTransferObject.Headers;

public final class TransactionConfigurationCache
    extends AbstractLocalCache<String, Configuration, TransactionConfigurationCache> {

    @Override
    protected String keyLabel() {
        return Headers.X_CORRELATION_ID;
    }

    @Override
    protected String valueLabel() {
        return "Jooq Configuration";
    }

}
