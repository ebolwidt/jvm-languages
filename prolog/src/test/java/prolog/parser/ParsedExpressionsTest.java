package prolog.parser;

import junit.framework.TestCase;
import prolog.PrologEngine;
import prolog.Result;
import prolog.Rule;
import prolog.expression.Constant;
import prolog.expression.Predicate;

/**
 * Tests whether expressions are parsed correctly with respect to priority and associativity.
 */
public class ParsedExpressionsTest extends TestCase {

    /**
     * Tests that the minus expression is parsed in a left-associative manner. <br>
     * Left associative: 5 - 4 - 1 = (5 - 4) - 1 = 0<br>
     * Right associative: 5 - 4 - 1 = 5 - (4 - 1) = 2
     */
    public void testAssociativityPlusMinus() {
        String program = "calc(X) :- X is 5 - 4 - 1.";
        PrologParser parser = new PrologParser(program);
        Rule rule = parser.parseRule();
        PrologEngine engine = new PrologEngine();
        engine.addRule(rule);

        // Variable variableY = new Variable("Y");
        Predicate question = new Predicate("calc", 1, new Constant(0));
        Result answer = engine.askQuestion(question);
        assertTrue(answer.hasOneAnswer());
        // assertEquals(new Constant(0), ((Constant) variableY.getBinding()).getValue());

    }
}
