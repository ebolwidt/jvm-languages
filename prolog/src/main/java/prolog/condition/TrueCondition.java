package prolog.condition;

import java.util.Map;

import prolog.Execution;
import prolog.executor.TrueExecutor;
import prolog.expression.Variable;

public class TrueCondition extends Condition {

    /**
     * Immutable hence return this.
     */
    @Override
    public TrueCondition duplicate(Map<Variable, Variable> variableScope) {
        return this;
    }

    @Override
    public void evaluate(Execution execution) {
        execution.push(new TrueExecutor());
    }
}
