package prolog.executor;

import java.util.Iterator;

import prolog.Execution;
import prolog.condition.Condition;

public class DisjunctionExecutor extends Executor {
    private Iterator<Condition> conditions;

    public DisjunctionExecutor(Iterator<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public Boolean execute(Execution execution) {
        System.out.println("! DisjunctionExecutor");
        resetBindings();
        if (!conditions.hasNext()) {
            return Boolean.FALSE;
        }
        Condition condition = conditions.next();
        condition.evaluate(execution);
        return null;
    }
}
