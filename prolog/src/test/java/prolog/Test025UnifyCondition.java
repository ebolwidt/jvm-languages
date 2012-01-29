package prolog;

import junit.framework.TestCase;
import prolog.condition.UnifyCondition;
import prolog.expression.Constant;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class Test025UnifyCondition extends TestCase {

    public void testUnifyConditionConstantMatch() {
        PrologEngine engine = setupEngine();

        Result result = engine.askQuestion(new Predicate("hello", 1, new Constant("world")));
        assertTrue(result.hasAnswer());
        result.next();
        assertFalse("Only one answer expected", result.hasAnswer());
    }

    public void testUnifyConditionConstantMismatch() {
        PrologEngine engine = setupEngine();

        Result result1 = engine.askQuestion(new Predicate("hello", 1, new Constant(42)));
        assertFalse(result1.hasAnswer());
    }

    public void testUnifyConditionVariable() {
        PrologEngine engine = setupEngine();

        Variable variableY = new Variable("Y");
        Result result = engine.askQuestion(new Predicate("hello", 1, variableY));
        assertTrue(result.hasAnswer());
        assertEquals(new Constant("world"), variableY.getBinding());
        result.next();
        assertFalse("Only one answer expected", result.hasAnswer());
    }

    private PrologEngine setupEngine() {
        PrologEngine engine = new PrologEngine();

        Variable variableX = new Variable("X");
        UnifyCondition condition = new UnifyCondition(variableX, new Constant("world"));
        engine.addRule(new Rule(new Predicate("hello", 1, variableX), condition));
        return engine;
    }
}
