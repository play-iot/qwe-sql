package io.github.zero88.msa.sql.workflow;

public interface DMLTransactionWorkflow extends DMLWorkflow, SQLTransactionWorkflow {

    @Override
    default boolean continueOnError() {
        return false;
    }

}
