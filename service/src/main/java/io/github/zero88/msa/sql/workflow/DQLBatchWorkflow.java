package io.github.zero88.msa.sql.workflow;

import io.github.zero88.msa.sql.workflow.step.DQLBatchStep;

import lombok.NonNull;

/**
 * DQL workflow is used for querying list data from database.
 *
 * @see DQLWorkflow
 * @since 1.0.0
 */
public interface DQLBatchWorkflow extends SQLWorkflow {

    /**
     * Declares {@code DQL Batch step}
     *
     * @return the DQL batch step
     * @see DQLBatchStep
     * @since 1.0.0
     */
    @NonNull DQLBatchStep sqlStep();

}
