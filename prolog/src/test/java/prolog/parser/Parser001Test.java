package prolog.parser;

import junit.framework.TestCase;
import prolog.PredicateKey;
import prolog.Rule;
import prolog.condition.Condition;
import prolog.condition.Conjunction;
import prolog.condition.Disjunction;
import prolog.expression.Constant;
import prolog.expression.Expression;
import prolog.expression.Predicate;
import prolog.expression.Variable;

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

    public void testCondition() {
        String conditionStr = "father(X, Y), X = Y, true";
        PrologParser parser = new PrologParser(conditionStr);
        Condition condition = parser.parseCondition();
        assertTrue(condition.getClass().getName(), condition instanceof Conjunction);
        Conjunction conjunction = (Conjunction) condition;
        assertEquals(3, conjunction.getConditions().size());
    }

    public void testConditionDisjunction() {
        String conditionStr = "father(X, Y); X = Y, (true; Y = _)";
        PrologParser parser = new PrologParser(conditionStr);
        Condition condition = parser.parseCondition();
        assertTrue(condition.getClass().getName(), condition instanceof Disjunction);
        Disjunction disjunction = (Disjunction) condition;
        Conjunction right = (Conjunction) disjunction.getCondition(1);
        assertTrue(right.getCondition(1).getClass().getName(), right.getCondition(1) instanceof Disjunction);
    }

    public void testBadSyntax() {
        String badProgram = "@@@@@@";
        PrologParser parser = new PrologParser(badProgram);
        try {
            parser.parseCondition();
            fail("Bad program should have caused a SyntaxException");
        } catch (SyntaxException e) {
            // Success
            assertEquals(new Token(TokenType.INVALID, "@"), e.getToken());
            assertEquals("prolog.parser.SyntaxException: Token(INVALID, @)", e.toString());
        }
    }

    public void testParseList1() {
        String program = "[H|T]";
        PrologParser parser = new PrologParser(program);
        Expression expression = parser.parseExpression();

        assertTrue(expression instanceof Predicate);
        Predicate predicate = (Predicate) expression;
        assertEquals(new PredicateKey(".", 2), predicate.getKey());
        assertEquals(new Variable("H"), predicate.getExpressions()[0]);
        assertEquals(new Variable("T"), predicate.getExpressions()[1]);
    }

    public void testParseList2() {
        String program = "[a, \"b\", 1, c(X), [ d ]]";
        PrologParser parser = new PrologParser(program);
        Expression expression = parser.parseExpression();

        assertTrue(expression instanceof Predicate);
        Predicate predicate = (Predicate) expression;
        assertEquals(new PredicateKey(".", 2), predicate.getKey());
        assertEquals(new Predicate("a", 0), predicate.getExpressions()[0]);
        // TODO: test rest
    }

    public void testParseList3() {
        String program = "[a, b | T]";
        PrologParser parser = new PrologParser(program);
        Expression expression = parser.parseExpression();

        assertTrue(expression instanceof Predicate);
        Predicate predicate = (Predicate) expression;
        assertEquals(new PredicateKey(".", 2), predicate.getKey());
        assertEquals(new Predicate("a", 0), predicate.getExpressions()[0]);
        predicate = (Predicate) predicate.getExpressions()[1];
        assertEquals(new PredicateKey(".", 2), predicate.getKey());
        assertEquals(new Predicate("b", 0), predicate.getExpressions()[0]);
        assertEquals(new Variable("T"), predicate.getExpressions()[1]);
    }

    public void testParseRule1() {
        String program = "length([], 0).";
        PrologParser parser = new PrologParser(program);

        Rule rule = parser.parseRule();
        Predicate predicate = rule.getHead();
        assertEquals(new PredicateKey("length", 2), predicate.getKey());
        assertEquals(new Predicate("[]", 0), predicate.getExpressions()[0]);
        assertEquals(new Constant(0), predicate.getExpressions()[1]);
    }

    public void testParseRule2() {
        String program = "length([H|T], L) :- length(T, LT), L is 1 + LT.";
        PrologParser parser = new PrologParser(program);

        Rule rule = parser.parseRule();
        Predicate predicate = rule.getHead();
        assertEquals(new PredicateKey("length", 2), predicate.getKey());

    }

}
