package prolog;

import junit.framework.TestCase;
import prolog.condition.IsCondition;
import prolog.expression.Constant;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class Test045IsCalculations extends TestCase {

    public void testIsAdditionWithConstantMatch() {
        PrologEngine engine = setupEngineForAddition();

        // a(2, 5)?
        Result answer = engine.askQuestion(new Predicate("a", 2, new Constant(2), new Constant(5)));
        assertTrue(answer.hasOneAnswer());
    }

    public void testIsAdditionWithConstantMismatch() {
        PrologEngine engine = setupEngineForAddition();

        // a(10, 5)?
        Result answer = engine.askQuestion(new Predicate("a", 2, new Constant(10), new Constant(5)));
        assertFalse(answer.hasAnswer());
    }

    public void testIsAdditionWithVariable() {
        PrologEngine engine = setupEngineForAddition();

        // a(2, Z) ==> Z = 5
        Variable variableZ = new Variable("Z");
        Result answer = engine.askQuestion(new Predicate("a", 2, new Constant(2), variableZ));
        assertTrue(answer.hasAnswer());
        assertEquals(new Constant(5), variableZ.getBinding());
        answer.next();
        assertFalse("One answer expected, no more", answer.hasAnswer());
    }

    private PrologEngine setupEngineForAddition() {
        PrologEngine engine = new PrologEngine();

        // a(X, Y) :- Y is X + 3

        Variable variableX = new Variable("X");
        Variable variableY = new Variable("Y");

        Predicate plusThree = new Predicate("+", 2, variableX, new Constant(3));
        IsCondition condition = new IsCondition(variableY, plusThree);
        engine.addRule(new Rule(new Predicate("a", 2, variableX, variableY), condition));
        return engine;
    }

}
