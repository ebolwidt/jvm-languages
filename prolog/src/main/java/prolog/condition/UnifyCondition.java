package prolog.condition;

import java.util.Map;

import prolog.Execution;
import prolog.executor.UnifyExecutor;
import prolog.expression.Expression;
import prolog.expression.Variable;

public class UnifyCondition extends Condition {
    private Expression left;
    private Expression right;

    public UnifyCondition(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public UnifyCondition duplicate(Map<Variable, Variable> variableScope) {
        return new UnifyCondition(left.duplicate(variableScope), right.duplicate(variableScope));
    }

    @Override
    public void evaluate(Execution execution) {
        execution.push(new UnifyExecutor(left, right));
    }

}
