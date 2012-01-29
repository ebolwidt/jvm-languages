package prolog.condition;

import java.util.List;
import java.util.Map;

import prolog.Execution;
import prolog.Rule;
import prolog.executor.FalseExecutor;
import prolog.executor.MatchExecutor;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class MatchCondition extends Condition {

    private Predicate predicate;

    public MatchCondition(Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public Condition duplicate(Map<Variable, Variable> variableScope) {
        return new MatchCondition(predicate.duplicate(variableScope));
    }

    @Override
    public void evaluate(Execution execution) {
        List<Rule> rules = execution.getEngine().findRules(predicate.getKey());
        if (rules == null) {
            execution.push(new FalseExecutor());
        } else {
            execution.push(new MatchExecutor(predicate, rules.iterator()));
        }
    }

    public Predicate getPredicate() {
        return predicate;
    }

}
