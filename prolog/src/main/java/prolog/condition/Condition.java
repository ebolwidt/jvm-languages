package prolog.condition;

import java.util.Map;

import prolog.Execution;
import prolog.expression.Variable;

public abstract class Condition {

    public abstract Condition duplicate(Map<Variable, Variable> variableScope);

    public abstract void evaluate(Execution execution);
}
