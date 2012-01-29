package prolog.condition;

import java.util.Map;

import prolog.Execution;
import prolog.executor.IsExecutor;
import prolog.expression.Expression;
import prolog.expression.Variable;

public class IsCondition extends Condition {
    private Expression left;
    private Expression right;

    public IsCondition(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public IsCondition duplicate(Map<Variable, Variable> variableScope) {
        return new IsCondition(left.duplicate(variableScope), right.duplicate(variableScope));
    }

    @Override
    public void evaluate(Execution execution) {
        execution.push(new IsExecutor(left, right));
    }

}
