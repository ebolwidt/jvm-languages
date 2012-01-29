package prolog.expression;

import java.util.Map;

import prolog.Type;
import prolog.executor.Executor;

public class Constant extends Expression {
    private Type type;

    private Object value;

    public Constant(String string) {
        type = Type.STRING;
        value = string;
    }

    public Constant(long integral) {
        type = Type.INTEGRAL;
        value = integral;
    }

    /**
     * Immutable, hence return this.
     */
    @Override
    public Constant duplicate(Map<Variable, Variable> variableScope) {
        return this;
    }

    @Override
    public boolean isBound() {
        return true;
    }

    @Override
    public Expression getBinding() {
        return this;
    }

    @Override
    public boolean unify(Executor bindings, Expression other) {
        if (!other.isBound()) {
            return other.unify(bindings, this);
        }
        other = other.getBinding();
        if (other instanceof Constant) {
            Constant otherConstant = (Constant) other;
            if (type == otherConstant.type && value.equals(otherConstant.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        switch (type) {
        case STRING:
            return "\"" + value + '"';
        default:
            return String.valueOf(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Constant other = (Constant) o;

        return (type == other.type && value.equals(other.value));
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
