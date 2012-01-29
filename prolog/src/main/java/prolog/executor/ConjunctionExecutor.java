package prolog.executor;

import java.util.Iterator;

import prolog.Execution;
import prolog.condition.Condition;

public class ConjunctionExecutor extends Executor {
    private Iterator<Condition> conditions;

    public ConjunctionExecutor(Iterator<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public Boolean execute(Execution execution) {
        System.out.println("! ConjunctionExecutor");
        resetBindings();
        if (!conditions.hasNext()) {
            return Boolean.FALSE;
        }
        Condition condition = conditions.next();
        condition.evaluate(execution);
        return null;
    }

    public boolean pushMore(Execution execution) {
        System.out.println("! ConjunctionExecutor.pushMore");
        if (!conditions.hasNext()) {
            return false;
        }
        Condition condition = conditions.next();
        condition.evaluate(execution);
        return true;
    }

}
