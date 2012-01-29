package prolog;

import junit.framework.TestCase;
import prolog.expression.Constant;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class Test02Variables extends TestCase {

    public void testNoConditionSimpleOneMatch() {
        PrologEngine engine = new PrologEngine();

        // father("Kees", "Piet"). # Piet is the father of Kees
        engine.addRule(new Rule(new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"))));

        Variable variableX = new Variable("X");
        Predicate askFather = new Predicate("father", 2, new Constant("Kees"), variableX);

        Result result = engine.askQuestion(askFather);
        assertTrue(result.hasAnswer());
        assertTrue(variableX.isBound());
        assertEquals(new Constant("Piet"), variableX.getBinding());
    }

    public void testNoConditionComplexOneMatch() {
        PrologEngine engine = new PrologEngine();

        // a(b("c","d")
        Predicate predicate = new Predicate("a", 1, new Predicate("b", 2, new Constant("c"), new Constant("d")));
        Rule rule = new Rule(predicate);
        engine.addRule(rule);

        {
            Variable variableX = new Variable("X");
            Predicate ask = new Predicate("a", 1, variableX);
            Result result = engine.askQuestion(ask);
            assertTrue(result.hasAnswer());
            assertTrue(variableX.isBound());
            assertEquals(new Predicate("b", 2, new Constant("c"), new Constant("d")), variableX.getBinding());
        }

        {
            Variable variableY = new Variable("Y");
            Variable variableZ = new Variable("Z");
            Predicate ask = new Predicate("a", 1, new Predicate("b", 2, variableY, variableZ));
            Result result = engine.askQuestion(ask);
            assertTrue(result.hasAnswer());
            assertTrue(variableY.isBound());
            assertTrue(variableZ.isBound());
            assertEquals(new Constant("c"), variableY.getBinding());
            assertEquals(new Constant("d"), variableZ.getBinding());
        }

    }

    public void testNoConditionSimplThreeMatches() {
        PrologEngine engine = new PrologEngine();

        // father("Kees", "Piet").
        engine.addRule(new Rule(new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"))));
        // father("Marie", "Piet").
        engine.addRule(new Rule(new Predicate("father", 2, new Constant("Marie"), new Constant("Piet"))));
        // father("Arnold", "Piet").
        engine.addRule(new Rule(new Predicate("father", 2, new Constant("Arnold"), new Constant("Piet"))));

        Variable variableX = new Variable("X");
        Predicate askFather = new Predicate("father", 2, variableX, new Constant("Piet"));

        Result result = engine.askQuestion(askFather);

        assertTrue(result.hasAnswer());
        assertTrue(variableX.isBound());
        assertEquals(new Constant("Kees"), variableX.getBinding());

        result.next();

        assertTrue(result.hasAnswer());
        assertTrue(variableX.isBound());
        assertEquals(new Constant("Marie"), variableX.getBinding());

        result.next();

        assertTrue(result.hasAnswer());
        assertTrue(variableX.isBound());
        assertEquals(new Constant("Arnold"), variableX.getBinding());

        result.next();

        assertFalse(result.hasAnswer());

    }

    public void testRuleBindingNotPermanent() {
        PrologEngine engine = new PrologEngine();

        // thing(X).
        engine.addRule(new Rule(new Predicate("thing", 1, new Variable("X"))));
        
        
        Result result1 = engine.askQuestion(new Predicate("thing", 1, new Constant(42)));
        assertTrue(result1.hasAnswer());
        Result result2 = engine.askQuestion(new Predicate("thing", 1, new Constant("Hello world")));
        assertTrue(result2.hasAnswer());
    }
}
