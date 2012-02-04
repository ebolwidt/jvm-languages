package prolog.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import prolog.Execution;
import prolog.executor.DisjunctionExecutor;
import prolog.expression.Variable;

public class Disjunction extends Condition {

    private List<Condition> conditions;

    public Disjunction(Condition... conditions) {
        this(Arrays.asList(conditions));
    }

    public Disjunction(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public Condition getCondition(int index) {
        return conditions.get(index);
    }

    @Override
    public Disjunction duplicate(Map<Variable, Variable> variableScope) {
        List<Condition> duplicates = new ArrayList<Condition>(conditions.size());
        for (Condition condition : conditions) {
            duplicates.add(condition.duplicate(variableScope));
        }
        return new Disjunction(duplicates);
    }

    @Override
    public void evaluate(Execution execution) {
        execution.push(new DisjunctionExecutor(conditions.iterator()));
    }
}
