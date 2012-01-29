package prolog.expression;

import java.util.Map;

import prolog.PredicateKey;
import prolog.Type;
import prolog.executor.Executor;

public class Predicate extends Expression {
    private PredicateKey key;
    private Expression[] expressions;

    public Predicate(String name, int arity, Expression... expressions) {
        this(new PredicateKey(name, arity), expressions);
    }

    public Predicate(PredicateKey key, Expression... expressions) {
        if (expressions.length != key.getArity())
            throw new IllegalArgumentException("Wrong number of expressions for arity");
        this.key = key;
        this.expressions = expressions;
    }

    @Override
    public Predicate duplicate(Map<Variable, Variable> variableScope) {
        Expression[] duplicateExpressions = new Expression[expressions.length];

        for (int e = 0; e < expressions.length; e++) {
            duplicateExpressions[e] = expressions[e].duplicate(variableScope);
        }

        return new Predicate(key, duplicateExpressions);
    }

    @Override
    public boolean isBound() {
        for (Expression expression : expressions) {
            if (!expression.isBound()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Type getType() {
        return Type.PREDICATE;
    }

    @Override
    public Expression getBinding() {
        return this;
    }

    public boolean unify(Executor bindings, Expression other) {
        if (!other.isBound()) {
            return other.unify(bindings, this);
        }
        other = other.getBinding();
        if (getClass() != other.getClass()) {
            return false;
        } else {
            return unify(bindings, (Predicate) other);
        }
    }

    public boolean unify(Executor bindings, Predicate other) {
        if (!key.equals(other.getKey()))
            return false;
        for (int e = 0; e < expressions.length; e++) {
            Expression e1 = expressions[e];
            Expression e2 = other.expressions[e];
            if (!e1.unify(bindings, e2)) {
                return false;
            }
        }
        return true;
    }

    public PredicateKey getKey() {
        return key;
    }

    public Expression[] getExpressions() {
        return expressions;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(100);
        b.append(key).append("(");
        for (int e = 0; e < expressions.length; e++) {
            if (e > 0)
                b.append(", ");
            b.append(expressions[e]);
        }
        b.append(")");
        return b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Predicate other = (Predicate) o;

        if (!key.equals(other.key))
            return false;

        for (int e = 0; e < expressions.length; e++) {
            Expression e1 = expressions[e];
            Expression e2 = other.expressions[e];
            if (!e1.equals(e2)) {
                return false;
            }
        }

        return true;
    }
}
