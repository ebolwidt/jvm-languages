package prolog.expression;

import java.util.Map;

import prolog.Type;
import prolog.executor.Executor;

public class Variable extends Expression {

    private String name;

    private boolean bound;

    private Expression binding;

    public Variable(String name) {
        super();
        this.name = name;
    }

    @Override
    public Variable duplicate(Map<Variable, Variable> variableScope) {
        if (!variableScope.containsKey(this)) {
            variableScope.put(this, new Variable(name));
        }
        return variableScope.get(this);
    }

    public void unbind() {
        bound = false;
        binding = null;
    }

    public boolean isBound() {
        return bound && binding.isBound();
    }

    @Override
    public Type getType() {
        if (!bound)
            return Type.UNBOUND;
        else
            return binding.getType();
    }

    @Override
    public Expression getBinding() {
        if (!bound)
            return this;
        else
            return binding.getBinding();
    }

    @Override
    public boolean unify(Executor bindings, Expression other) {
        if (bound)
            return binding.unify(bindings, other);
        bound = true;
        binding = other;
        bindings.addBinding(this);
        return true;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("@").append(name);
        if (isBound()) {
            b.append('[').append(getBinding()).append(']');
        }

        return b.toString();
    }

    public Expression getDirectBinding() {
        return binding;
    }

}
