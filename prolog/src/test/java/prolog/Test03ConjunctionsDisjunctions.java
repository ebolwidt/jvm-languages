package prolog;

import junit.framework.TestCase;
import prolog.condition.Conjunction;
import prolog.condition.Disjunction;
import prolog.condition.UnifyCondition;
import prolog.expression.Constant;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class Test03ConjunctionsDisjunctions extends TestCase {

    public void testDisjunctionBound() {
        PrologEngine engine = setupDisjunction();

        assertTrue(engine.askQuestion(new Predicate("hello", 1, new Constant("world"))).hasOneAnswer());
        assertTrue(engine.askQuestion(new Predicate("hello", 1, new Constant("mars"))).hasOneAnswer());
        assertTrue(engine.askQuestion(new Predicate("hello", 1, new Constant("venus"))).hasOneAnswer());
        assertFalse(engine.askQuestion(new Predicate("hello", 1, new Constant("pluto"))).hasAnswer());
        assertFalse(engine.askQuestion(new Predicate("hello", 1, new Constant(42))).hasAnswer());
    }

    public void testDisjunctionVariable() {
        PrologEngine engine = setupDisjunction();

        Variable variableY = new Variable("Y");
        Result result3 = engine.askQuestion(new Predicate("hello", 1, variableY));

        assertTrue(result3.hasAnswer());
        assertEquals(new Constant("world"), variableY.getBinding());

        result3.next();
        assertTrue(result3.hasAnswer());
        assertEquals(new Constant("mars"), variableY.getBinding());

        result3.next();
        assertTrue(result3.hasAnswer());
        assertEquals(new Constant("venus"), variableY.getBinding());

    }

    public void testConjunctionBoundMatch() {
        PrologEngine engine = setupConjunction();

        Predicate question = new Predicate("three", 3, new Constant("one"), new Constant("two"), new Constant("two"));
        assertTrue("Unexpected: no match found for question", engine.askQuestion(question).hasOneAnswer());
    }

    public void testConjunctionBoundMismatch() {
        PrologEngine engine = setupConjunction();

        Predicate question = new Predicate("three", 3, new Constant("one"), new Constant("two"), new Constant("three"));
        assertFalse(engine.askQuestion(question).hasAnswer());
    }

    public void testConjunctionVariable() {
        PrologEngine engine = setupConjunction();

        Variable variableX = new Variable("X");
        Variable variableY = new Variable("Y");
        Variable variableZ = new Variable("Z");

        Predicate question = new Predicate("three", 3, variableX, variableY, variableZ);
        Result answer = engine.askQuestion(question);
        assertTrue(answer.hasAnswer());
        assertEquals(new Constant("one"), variableX.getBinding());
        assertEquals(new Constant("two"), variableY.getBinding());
        assertEquals(new Constant("two"), variableZ.getBinding());
        answer.next();
        assertFalse(answer.hasAnswer());
    }

    private PrologEngine setupDisjunction() {
        PrologEngine engine = new PrologEngine();

        Variable variableX = new Variable("X");
        UnifyCondition condition1 = new UnifyCondition(variableX, new Constant("world"));
        UnifyCondition condition2 = new UnifyCondition(variableX, new Constant("mars"));
        UnifyCondition condition3 = new UnifyCondition(variableX, new Constant("venus"));
        Disjunction condition = new Disjunction(condition1, condition2, condition3);
        engine.addRule(new Rule(new Predicate("hello", 1, variableX), condition));
        return engine;
    }

    private PrologEngine setupConjunction() {
        PrologEngine engine = new PrologEngine();

        Variable variableX = new Variable("X");
        Variable variableY = new Variable("Y");
        Variable variableZ = new Variable("Z");
        UnifyCondition condition1 = new UnifyCondition(variableX, new Constant("one"));
        UnifyCondition condition2 = new UnifyCondition(variableY, new Constant("two"));
        UnifyCondition condition3 = new UnifyCondition(variableZ, variableY);
        Conjunction condition = new Conjunction(condition1, condition2, condition3);
        // three(X, Y, Z) :- X = "one", Y = "two", Z = Y
        engine.addRule(new Rule(new Predicate("three", 3, variableX, variableY, variableZ), condition));
        return engine;
    }
}
