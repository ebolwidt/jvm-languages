package prolog;

import prolog.condition.MatchCondition;
import prolog.expression.Constant;
import prolog.expression.Predicate;
import junit.framework.TestCase;

public class Test01NoVariables extends TestCase {

    public void testSimpleConstantsStringTrue() {
        PrologEngine engine = new PrologEngine();

        // father("Kees", "Piet"). # Piet is the father of Kees
        Predicate father = new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"));
        Rule rule = new Rule(father);
        engine.addRule(rule);

        Predicate askFather = new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"));

        Result result = engine.askQuestion(askFather);
        assertTrue(result.hasAnswer());
    }

    public void testSimpleConstantsIntegralTrue() {
        PrologEngine engine = new PrologEngine();

        // age("Piet", 34)
        Predicate ageRulePredicate = new Predicate("age", 2, new Constant("Piet"), new Constant(34));
        Rule ageRule = new Rule(ageRulePredicate);
        engine.addRule(ageRule);

        Predicate ask = new Predicate("age", 2, new Constant("Piet"), new Constant(34));
        Result result = engine.askQuestion(ask);
        assertTrue(result.hasAnswer());
    }

    public void testSimpleConstantsNoAnswerNoDefinitions() {
        PrologEngine engine = new PrologEngine();

        Predicate askFather = new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"));

        Result result = engine.askQuestion(askFather);
        assertFalse(result.hasAnswer());
    }

    public void testSimpleConstantsNoAnswerDifferentPredicate() {
        PrologEngine engine = new PrologEngine();

        // mother("Marie", "Rosa"). # Rosa is the mother of Marie
        Predicate mother = new Predicate("mother", 2, new Constant("Marie"), new Constant("Rosa"));
        Rule rule = new Rule(mother);
        engine.addRule(rule);

        Predicate askFather = new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"));

        Result result = engine.askQuestion(askFather);
        assertFalse(result.hasAnswer());
    }

    public void testSimpleConstantsNoAnswerSamePredicateDifferentExpressions() {
        PrologEngine engine = new PrologEngine();

        // father("Henk", "Johan"). # Johan is the father of Henk
        Predicate father = new Predicate("father", 2, new Constant("Henk"), new Constant("Johan"));
        Rule rule = new Rule(father);
        engine.addRule(rule);

        Predicate askFather = new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"));

        Result result = engine.askQuestion(askFather);
        assertFalse(result.hasAnswer());
    }

    public void testConditionalConstantsTrue() {
        PrologEngine engine = new PrologEngine();

        // age("Piet", 34)
        Predicate ageRulePredicate = new Predicate("age", 2, new Constant("Piet"), new Constant(34));
        Rule ageRule = new Rule(ageRulePredicate);
        engine.addRule(ageRule);

        // father("Kees", "Piet") :- age("Piet", 34). # Piet is the father of Kees if the age of Piet is 34
        Predicate father = new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"));
        Predicate ageMatchPredicate = new Predicate("age", 2, new Constant("Piet"), new Constant(34));
        Rule rule = new Rule(father, new MatchCondition(ageMatchPredicate));
        engine.addRule(rule);

        Predicate askFather = new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"));

        Result result = engine.askQuestion(askFather);
        assertTrue("Incorrect answer", result.hasAnswer());
    }

    public void testConditionalConstantsFalse() {
        PrologEngine engine = new PrologEngine();

        // age("Piet", 34)
        Predicate ageRulePredicate = new Predicate("age", 2, new Constant("Piet"), new Constant(25));
        Rule ageRule = new Rule(ageRulePredicate);
        engine.addRule(ageRule);

        // father("Kees", "Piet") :- age("Piet", 34). # Piet is the father of Kees if the age of Piet is 34
        Predicate father = new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"));
        Predicate ageMatchPredicate = new Predicate("age", 2, new Constant("Johan"), new Constant(34));
        Rule rule = new Rule(father, new MatchCondition(ageMatchPredicate));
        engine.addRule(rule);

        Predicate askFather = new Predicate("father", 2, new Constant("Kees"), new Constant("Piet"));

        Result result = engine.askQuestion(askFather);
        assertFalse(result.hasAnswer());
    }
}
