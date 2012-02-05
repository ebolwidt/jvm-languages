package prolog;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import prolog.condition.Condition;
import prolog.condition.Conjunction;
import prolog.condition.IsCondition;
import prolog.condition.MatchCondition;
import prolog.expression.Constant;
import prolog.expression.Expression;
import prolog.expression.Predicate;
import prolog.expression.Variable;
import prolog.parser.PrologParser;

public class Test050Recursion extends TestCase {

    public void testLengthOfListJavaBuiltRules() {
        PrologEngine engine = setupEngineWithLengthRules(false);
        checkLengthOfListResult(engine);
    }

    public void testLengthOfListParser() {
        PrologEngine engine = setupEngineWithLengthRules(true);
        checkLengthOfListResult(engine);
    }

    private void checkLengthOfListResult(PrologEngine engine) {
        Predicate list = createPrologList(new Constant("Quick"), new Constant("Brown"), new Constant("Fox"),
                new Constant("Jumps"));
        Variable variableL = new Variable("L");
        Result answer = engine.askQuestion(new Predicate("length", 2, list, variableL));
        assertTrue(answer.hasAnswer());
        assertEquals(new Constant(4), variableL.getBinding());
        answer.next();
        assertFalse("Only one answer expected", answer.hasAnswer());
    }

    private PrologEngine setupEngineWithLengthRules(boolean useParser) {
        PrologEngine engine = new PrologEngine();

        if (useParser) {
            String line1 = "length([], 0).";
            String line2 = "length([H|T], L) :- length(T, LT), L is 1 + LT.";
            PrologParser parser = new PrologParser(line1 + "\n" + line2);
            engine.addRule(parser.parseRule());
            engine.addRule(parser.parseRule());
        } else {
            // length([], 0).
            // length([H|T], L) :- length(T, LT), L is 1 + LT.

            engine.addRule(new Rule(new Predicate("length", 2, new Predicate("[]", 0), new Constant(0))));

            Variable variableH = new Variable("H");
            Variable variableT = new Variable("T");
            Variable variableL = new Variable("L");
            Variable variableLT = new Variable("LT");
            Condition lengthOfTailCondition = new MatchCondition(new Predicate("length", 2, variableT, variableLT));
            Condition plusOneCondition = new IsCondition(variableL, new Predicate("+", 2, new Constant(1), variableLT));
            Condition condition = new Conjunction(lengthOfTailCondition, plusOneCondition);
            engine.addRule(new Rule(new Predicate("length", 2, new Predicate(".", 2, variableH, variableT), variableL), condition));
        }

        return engine;
    }

    public static Predicate createPrologList(Expression... list) {
        return createPrologList(Arrays.asList(list));

    }

    public static Predicate createPrologList(List<Expression> list) {
        if (list.isEmpty()) {
            return new Predicate("[]", 0);
        }
        return new Predicate(".", 2, list.get(0), createPrologList(list.subList(1, list.size())));
    }
}
