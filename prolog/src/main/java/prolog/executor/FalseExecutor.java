package prolog.executor;

import prolog.Execution;

public class FalseExecutor extends Executor {
    @Override
    public Boolean execute(Execution execution) {
        return Boolean.FALSE;
    }

}
