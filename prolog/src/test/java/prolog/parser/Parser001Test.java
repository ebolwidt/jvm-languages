package prolog.parser;

import junit.framework.TestCase;
import prolog.condition.Condition;
import prolog.condition.Conjunction;
import prolog.condition.Disjunction;
import prolog.expression.Constant;
import prolog.expression.Predicate;

public class Parser001Test extends TestCase {

    public void testPredicate() {
        String fact = "father('Piet', 'Jan').";
        PrologParser parser = new PrologParser(fact);
        Predicate head = parser.parsePredicate();
        assertEquals("father", head.getKey().getName());
        assertEquals(2, head.getKey().getArity());
        assertEquals(new Constant("Piet"), head.getExpressions()[0]);
        assertEquals(new Constant("Jan"), head.getExpressions()[1]);
    }

    public void testCondition() {
        String conditionStr = "father(X, Y), X = Y, true";
        PrologParser parser = new PrologParser(conditionStr);
        Condition condition = parser.parseCondition();
        assertTrue(condition.getClass().getName(), condition instanceof Conjunction);
        Conjunction conjunction = (Conjunction) condition;
        assertEquals(3, conjunction.getConditions().size());
    }

    public void testConditionDisjunction() {
        String conditionStr = "father(X, Y); X = Y, (true; Y = _)";
        PrologParser parser = new PrologParser(conditionStr);
        Condition condition = parser.parseCondition();
        assertTrue(condition.getClass().getName(), condition instanceof Disjunction);
        Disjunction disjunction = (Disjunction) condition;
        Conjunction right = (Conjunction) disjunction.getCondition(1);
        assertTrue(right.getCondition(1).getClass().getName(), right.getCondition(1) instanceof Disjunction);
    }

}
