package prolog;

import junit.framework.TestCase;
import prolog.executor.TrueExecutor;
import prolog.expression.Constant;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class Test00UnifyExpressions extends TestCase {

    public void testUnifyConstants() {
        Constant a = new Constant("a");
        Constant b = new Constant("a");

        TrueExecutor executor = new TrueExecutor();

        assertTrue(a.unify(executor, b));
        executor.resetBindings();
        assertTrue(b.unify(executor, a));
    }

    public void testUnifyVariableConstant() {
        Constant a = new Constant("a");
        Variable b = new Variable("B");

        TrueExecutor executor = new TrueExecutor();

        assertTrue(a.unify(executor, b));
        executor.resetBindings();
        assertTrue(b.unify(executor, a));
    }

    public void testUnifyVariablePredicate() {
        Predicate a = new Predicate("a", 1, new Constant("b"));
        Variable b = new Variable("B");

        TrueExecutor executor = new TrueExecutor();

        assertTrue(a.unify(executor, b));
        executor.resetBindings();
        assertTrue(b.unify(executor, a));
    }

    public void testUnifyPredicateWithVariableAndPredicate() {
        Predicate predicateA = new Predicate("a", 1, new Constant("b"));
        Variable variableB = new Variable("B");
        Predicate predicateB = new Predicate("a", 1, variableB);

        TrueExecutor executor = new TrueExecutor();

        assertTrue(predicateA.unify(executor, predicateB));
        executor.resetBindings();
        assertTrue(predicateB.unify(executor, predicateA));
    }

    public void testUnifyPredicateWithTwoUnifiedVariablesAndPredicateWithTwoConstants() {
        Predicate predicateA = new Predicate("a", 2, new Constant("b"), new Constant("b"));
        Variable variableB = new Variable("B");
        Predicate predicateB = new Predicate("a", 2, variableB, variableB);

        TrueExecutor executor = new TrueExecutor();

        assertTrue(predicateA.unify(executor, predicateB));
        executor.resetBindings();
        assertTrue(predicateB.unify(executor, predicateA));
    }

    public void testUnifyPredicateWithTwoUnifiedVariablesAndPredicateWithTwoPredicates() {
        Predicate predicateA = new Predicate("a", 2, new Predicate("b", 0), new Predicate("b", 0));
        Variable variableB = new Variable("B");
        Predicate predicateB = new Predicate("a", 2, variableB, variableB);

        TrueExecutor executor = new TrueExecutor();

        assertTrue(predicateA.unify(executor, predicateB));
        executor.resetBindings();
        assertTrue(predicateB.unify(executor, predicateA));
    }
}
