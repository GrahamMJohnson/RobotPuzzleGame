package cos420.robotrally.models;

import cos420.robotrally.enumerations.EAfterExecuteCondition;

/**
 * Callback interface for communicating between levelController and MainActivity (used to reduce
 * the strength of the coupling needed)
 */
public interface ExecutionCallback {
        void onStepHighlight(int index);
        void onExecutionEnd(EAfterExecuteCondition result);
}

