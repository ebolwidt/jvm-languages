package prolog.executor;

import prolog.Execution;
import prolog.expression.Expression;

public class UnifyExecutor extends Executor {
    private Expression left;
    private Expression right;
    private boolean executed;

    public UnifyExecutor(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean execute(Execution execution) {
        if (executed)
            return false;
        executed = true;
        System.out.println("Before unifying " + left + " with " + right);
        boolean result = left.unify(this, right);
        System.out.println("Unifying " + left + " with " + right + ": " + result);
        return result;
    }
}
