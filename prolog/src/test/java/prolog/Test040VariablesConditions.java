package prolog;

import junit.framework.TestCase;
import prolog.condition.Conjunction;
import prolog.condition.Condition;
import prolog.condition.MatchCondition;
import prolog.expression.Constant;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class Test040VariablesConditions extends TestCase {

    public void testConditionSimpleThreeMatches() {
        PrologEngine engine = new PrologEngine();

        {
            // father("Kees", "Piet").
            Rule rule = new Rule(new Predicate("father", 2, new Constant("Kees"), new Constant("Piet")));
            engine.addRule(rule);
        }
        {
            // father("Marie", "Piet").
            Rule rule = new Rule(new Predicate("father", 2, new Constant("Marie"), new Constant("Piet")));
            engine.addRule(rule);
        }
        {
            // father("Arnold", "Piet").
            Rule rule = new Rule(new Predicate("father", 2, new Constant("Arnold"), new Constant("Piet")));
            engine.addRule(rule);
        }
        {
            // father("Joep", "Freek").
            Rule rule = new Rule(new Predicate("father", 2, new Constant("Joep"), new Constant("Freek")));
            engine.addRule(rule);
        }

        {
            // paternal_sibling(X, Y) :- father(X, Z), father(Y, Z).
            // N.B. somebody is a sibling of himself under this limited definition, that's okay now.
            Variable variableX = new Variable("X");
            Variable variableY = new Variable("Y");
            Variable variableZ = new Variable("Z");
            Condition father1 = new MatchCondition(new Predicate("father", 2, variableX, variableZ));
            Condition father2 = new MatchCondition(new Predicate("father", 2, variableY, variableZ));
            Condition condition = new Conjunction(father1, father2);
            Rule rule = new Rule(new Predicate("paternal_sibling", 2, variableX, variableY), condition);
            engine.addRule(rule);
        }

        {
            Predicate question = new Predicate("paternal_sibling", 2, new Constant("Joep"), new Constant("Arnold"));
            Result result = engine.askQuestion(question);
            assertFalse(result.hasAnswer());
        }

        {
            Predicate question = new Predicate("paternal_sibling", 2, new Constant("Marie"), new Constant("Arnold"));
            Result result = engine.askQuestion(question);
            assertTrue(result.hasAnswer());
        }

    }

}
