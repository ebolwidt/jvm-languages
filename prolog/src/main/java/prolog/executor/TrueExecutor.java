package prolog.executor;

import prolog.Execution;

public class TrueExecutor extends Executor {
    private boolean hasExecuted = false;

    @Override
    public Boolean execute(Execution execution) {
        if (hasExecuted) {
            return Boolean.FALSE;
        } else {
            hasExecuted = true;
            return Boolean.TRUE;
        }
    }

}
