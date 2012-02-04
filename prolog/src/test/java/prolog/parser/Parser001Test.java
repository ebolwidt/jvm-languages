package prolog.parser;

import junit.framework.TestCase;
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

}
