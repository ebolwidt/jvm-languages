package prolog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prolog.condition.MatchCondition;
import prolog.expression.Predicate;

public class PrologEngine {
    private Map<PredicateKey, List<Rule>> rulesMap = new HashMap<PredicateKey, List<Rule>>();

    public void addRule(Rule rule) {
        PredicateKey predicateKey = rule.getHead().getKey();
        List<Rule> rules;

        if (!rulesMap.containsKey(predicateKey)) {
            rules = new ArrayList<Rule>();
            rulesMap.put(predicateKey, rules);
        } else {
            rules = rulesMap.get(predicateKey);
        }
        rules.add(rule);
    }

    public List<Rule> findRules(PredicateKey key) {
        return rulesMap.get(key);
    }

    public Result askQuestion(Predicate question) {
        Execution execution = new Execution(this);
        return askQuestion(execution, question);
    }

    public Result askQuestion(Execution execution, Predicate question) {
        new MatchCondition(question).evaluate(execution);
        return new Result(execution);
    }
}
