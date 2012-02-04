package prolog.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import prolog.Execution;
import prolog.executor.ConjunctionExecutor;
import prolog.expression.Variable;

public class Conjunction extends Condition {

    private List<Condition> conditions;

    public Conjunction(Condition... conditions) {
        this(Arrays.asList(conditions));
    }

    public Conjunction(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public Condition getCondition(int index) {
        return conditions.get(index);
    }

    @Override
    public Conjunction duplicate(Map<Variable, Variable> variableScope) {
        List<Condition> duplicates = new ArrayList<Condition>(conditions.size());
        for (Condition condition : conditions) {
            duplicates.add(condition.duplicate(variableScope));
        }
        return new Conjunction(duplicates);
    }

    @Override
    public void evaluate(Execution execution) {
        execution.push(new ConjunctionExecutor(conditions.iterator()));
    }
}
