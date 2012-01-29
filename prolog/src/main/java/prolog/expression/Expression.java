package prolog.expression;

import java.util.Map;

import prolog.Type;
import prolog.executor.Executor;

public abstract class Expression {
    public abstract Expression duplicate(Map<Variable, Variable> variableScope);

    public abstract boolean unify(Executor bindings, Expression other);

    public abstract boolean isBound();

    public abstract Expression getBinding();

    public abstract Type getType();
}
