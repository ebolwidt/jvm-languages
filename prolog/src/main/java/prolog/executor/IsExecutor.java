package prolog.executor;

import prolog.Execution;
import prolog.PredicateKey;
import prolog.Type;
import prolog.expression.Constant;
import prolog.expression.Expression;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class IsExecutor extends Executor {
    private Expression left;
    private Expression right;
    private boolean executed;

    public IsExecutor(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean execute(Execution execution) {
        if (executed)
            return false;
        executed = true;

        boolean result = left.unify(this, evaluate(right));

        return result;
    }

    public static Expression evaluate(Expression expression) {
        if (expression.isBound()) {
            expression = expression.getBinding();
        }

        if (expression instanceof Constant) {
            return evaluate((Constant) expression);
        } else if (expression instanceof Variable) {
            return evaluate((Variable) expression);
        } else if (expression instanceof Predicate) {
            return evaluate((Predicate) expression);
        } else {
            throw new IllegalArgumentException("Unknown type of expression");
        }
    }

    public static Expression evaluate(Constant expression) {
        return expression;
    }

    public static Expression evaluate(Variable expression) {
        return expression;
    }

    private static PredicateKey addOperation = new PredicateKey("+", 2);
    private static PredicateKey subtractOperation = new PredicateKey("-", 2);

    public static Expression evaluate(Predicate expression) {
        PredicateKey key = expression.getKey();
        if (key.equals(addOperation)) {
            Expression left = evaluate(expression.getExpressions()[0]);
            Expression right = evaluate(expression.getExpressions()[1]);
            left = left.getBinding();
            right = right.getBinding();
            // TODO: check types, return some error if incorrect type
            Constant cleft = (Constant) left;
            Constant cright = (Constant) right;
            if (cleft.getType() == Type.STRING) {
                return new Constant(String.valueOf(cleft.getValue()).concat(String.valueOf(cright.getValue())));
            } else {
                Number nleft = (Number) cleft.getValue();
                Number nright = (Number) cright.getValue();
                return new Constant(nleft.longValue() + nright.longValue());
            }
        } else if (key.equals(subtractOperation)) {
            Expression left = evaluate(expression.getExpressions()[0]);
            Expression right = evaluate(expression.getExpressions()[1]);
            left = left.getBinding();
            right = right.getBinding();
            // TODO: check types, return some error if incorrect type
            Constant cleft = (Constant) left;
            Constant cright = (Constant) right;

            Number nleft = (Number) cleft.getValue();
            Number nright = (Number) cright.getValue();
            return new Constant(nleft.longValue() - nright.longValue());

        } else {
            return expression;
        }
    }
}
