package io.github.zero88.msa.sql.service;

import io.github.zero88.msa.sql.handler.EntityHandler;
import io.github.zero88.msa.sql.EntityMetadata;

import lombok.NonNull;

interface BaseEntityService<M extends EntityMetadata> {

    /**
     * Declares entity handler
     *
     * @return entity handler
     * @see EntityHandler
     * @since 1.0.0
     */
    @NonNull EntityHandler entityHandler();

    /**
     * Declares physical database entity metadata
     *
     * @return entity metadata
     * @see EntityMetadata
     * @since 1.0.0
     */
    @NonNull M context();

}
