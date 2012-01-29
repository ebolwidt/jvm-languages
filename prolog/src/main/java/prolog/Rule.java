package prolog;

import java.util.IdentityHashMap;

import prolog.condition.Condition;
import prolog.condition.TrueCondition;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class Rule {
    private Predicate head;
    private Condition condition;

    public Rule(Predicate head) {
        this(head, new TrueCondition());
    }

    public Rule(Predicate head, Condition condition) {
        if (head == null)
            throw new NullPointerException("head");
        if (condition == null)
            throw new NullPointerException("condition");
        this.head = head;
        this.condition = condition;
    }

    /**
     * Duplicate rule to instantiate it for execution.
     * 
     * @return
     */
    public Rule instantiate() {
        IdentityHashMap<Variable, Variable> variableScope = new IdentityHashMap<Variable, Variable>();
        return new Rule(head.duplicate(variableScope), condition.duplicate(variableScope));
    }

    public Predicate getHead() {
        return head;
    }

    public Condition getCondition() {
        return condition;
    }
}
