package prolog.executor;

import java.util.Iterator;

import prolog.Execution;
import prolog.Rule;
import prolog.expression.Predicate;

public class MatchExecutor extends Executor {
    private Predicate predicate;

    private Iterator<Rule> rules;

    public MatchExecutor(Predicate predicate, Iterator<Rule> rules) {
        this.predicate = predicate;
        this.rules = rules;
    }

    @Override
    public Boolean execute(Execution execution) {
        resetBindings();
        if (!rules.hasNext()) {
            System.out.println("MatchExecutor: no more rules");
            return Boolean.FALSE;
        }
        Rule rule = rules.next();
        // Duplicate rule to instantiate it.
        rule = rule.instantiate();
        if (rule.getHead().unify(this, predicate)) {
            System.out.println("MatchExecutor: unified " + predicate + " with " + rule.getHead());
            rule.getCondition().evaluate(execution);
            return null;
        } else {
            System.out.println("MatchExecutor: our predicate " + predicate + " did not unify with " + rule.getHead());
            return null;
        }
    }
}
