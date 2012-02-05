package prolog.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prolog.PredicateKey;
import prolog.Rule;
import prolog.condition.Condition;
import prolog.condition.Conjunction;
import prolog.condition.Disjunction;
import prolog.condition.IsCondition;
import prolog.condition.MatchCondition;
import prolog.condition.UnifyCondition;
import prolog.expression.Constant;
import prolog.expression.Expression;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class PrologParser {

    private TokenStream tokenizer;

    /**
     * Valid during parsing of a rule, has to be cleared between rules or when desired.
     */
    private Map<String, Variable> variables = new HashMap<String, Variable>();

    public PrologParser(String program) {
        this.tokenizer = new TokenStream(new Tokenizer(program));
    }

    // public PrologParser(Tokenizer tokenizer) {
    // this.tokenizer = new TokenStream(tokenizer);
    // }

    public void clearVariables() {
        variables.clear();
    }

    public Expression parseAtom(Token token) {
        switch (token.getType()) {
        case NUMBER:
            return new Constant(Long.parseLong(token.getText()));
        case STRING:
            return new Constant(token.getText());
        case IDENTIFIER:
            // For this method, zero-arity predicates are also considered atoms.
            return new Predicate(new PredicateKey(token.getText(), 0));
        case EMPTY_LIST:
            // For this method, zero-arity predicates are also considered atoms.
            return new Predicate(new PredicateKey(token.getText(), 0));
        case VARIABLE:
            // TODO: variable
            // TODO: make sure that all variables in the same context point to the same variable
            // -- resolve on rule level, not in parser?
            String name = token.getText();
            if ("_".equals(name)) {
                // Variable that is not the same as any other variable, even if it has the same new.
                return new Variable("_");
            }
            if (variables.containsKey(name)) {
                return variables.get(name);
            } else {
                Variable v = new Variable(name);
                variables.put(name, v);
                return v;
            }
        default:
            throw new SyntaxException(token);
        }
    }

    public Predicate parsePredicate() {
        return parsePredicate(tokenizer.nextToken());
    }

    public Predicate parsePredicate(Token token) {
        tokenizer.consume(TokenType.LPAREN);
        List<Expression> arguments = new ArrayList<Expression>();
        while (true) {
            Token peek = tokenizer.peekToken();
            if (peek.getType() == TokenType.RPAREN)
                break;
            arguments.add(parseExpression());
            peek = tokenizer.peekToken();
            if (peek.getType() == TokenType.COMMA)
                tokenizer.consume(TokenType.COMMA);
            else if (peek.getType() == TokenType.RPAREN) {
                tokenizer.consume(TokenType.RPAREN);
                break;
            }
        }
        return new Predicate(new PredicateKey(token.getText(), arguments.size()), arguments.toArray(new Expression[arguments
                .size()]));
    }

    private static final PredicateKey LIST_PREDICATE_KEY = new PredicateKey(".", 2);
    private static final PredicateKey EMPTY_LIST_PREDICATE_KEY = new PredicateKey("[]", 0);

    public Expression parseList(Token token) {
        Expression head = parseExpression();
        Expression tail = new Predicate(EMPTY_LIST_PREDICATE_KEY);
        List<Expression> expressions = new ArrayList<Expression>();
        expressions.add(head);
        list_parsing: while (true) {
            token = tokenizer.nextToken();
            TokenType type = token.getType();
            switch (type) {
            case RBRACKET:
                break list_parsing;
            case COMMA:
                expressions.add(parseExpression());
                break;
            case PIPE:
                tail = parseExpression();
                break;
            default:
                throw new SyntaxException(token);
            }
        }

        while (!expressions.isEmpty()) {
            tail = new Predicate(LIST_PREDICATE_KEY, expressions.remove(expressions.size() - 1), tail);
        }
        return tail;
    }

    public Expression parseExpression() {
        Expression left = parseMultiplyDivideExpression();

        parse_loop: while (true) {
            Token token = tokenizer.peekToken();
            switch (token.getType()) {
            case PLUS:
            case MINUS:
                // TODO: this isn't right, it doesn't work with
                // 4 - 5 - 1 (which should be -1, 4 - (5 - 1) = 0)
                tokenizer.consume(token.getType());
                Expression right = parseBasicExpression();
                left = new Predicate(token.getText(), 2, left, right);
                break;
            default:
                break parse_loop;
            }
        }

        return left;
    }

    public Expression parseMultiplyDivideExpression() {
        Expression left = parseBasicExpression();
        Token token = tokenizer.peekToken();

        switch (token.getType()) {
        case MULTIPLY:
        case DIVIDE:
            // TODO: this isn't right, it doesn't work with
            // 4 - 5 - 1 (which should be -1, 4 - (5 - 1) = 0)
            tokenizer.consume(token.getType());
            Expression right = parseBasicExpression();
            return new Predicate(token.getText(), 2, left, right);
        }

        return left;
    }

    // The kind of expression that can appear in a predicate argument
    public Expression parseBasicExpression() {
        Token token = tokenizer.nextToken();
        switch (token.getType()) {
        case IDENTIFIER:
            // For this method, zero-arity predicates are also considered atoms.
            Token next = tokenizer.peekToken();
            if (next.getType() == TokenType.LPAREN)
                return parsePredicate(token);
            else
                return parseAtom(token);
        case LBRACKET:
            return parseList(token);
        default:
            return parseAtom(token);
        }
    }

    public Condition parseAtomicCondition() {
        Token token = tokenizer.peekToken();
        if (token.getType() == TokenType.LPAREN) {
            tokenizer.consume(TokenType.LPAREN);
            Condition condition = parseCondition();
            tokenizer.consume(TokenType.RPAREN);
            return condition;
        }

        Expression expression = parseExpression();
        token = tokenizer.peekToken();
        switch (token.getType()) {
        case IDENTIFIER:
            if ("is".equals(token.getText())) {
                tokenizer.consume(TokenType.IDENTIFIER);
                Expression right = parseExpression();
                return new IsCondition(expression, right);
            }
        case EQUALS:
            tokenizer.consume(TokenType.EQUALS);
            Expression right = parseExpression();
            return new UnifyCondition(expression, right);
        }
        if (expression instanceof Predicate) {
            return new MatchCondition((Predicate) expression);
        } else {
            // Unknown token!!
            System.out.println(expression);
            throw new SyntaxException(null);
        }
    }

    public Condition parseConjunction() {
        Condition condition = parseAtomicCondition();
        if (tokenizer.peekToken().getType() == TokenType.COMMA) {
            List<Condition> conditions = new ArrayList<Condition>();
            conditions.add(condition);
            while (tokenizer.peekToken().getType() == TokenType.COMMA) {
                tokenizer.consume(TokenType.COMMA);
                condition = parseAtomicCondition();
                conditions.add(condition);
            }
            condition = new Conjunction(conditions);
        }
        return condition;
    }

    public Condition parseDisjunction() {
        Condition condition = parseConjunction();
        if (tokenizer.peekToken().getType() == TokenType.SEMICOLON) {
            List<Condition> conditions = new ArrayList<Condition>();
            conditions.add(condition);
            while (tokenizer.peekToken().getType() == TokenType.SEMICOLON) {
                tokenizer.consume(TokenType.SEMICOLON);
                condition = parseConjunction();
                conditions.add(condition);
            }
            condition = new Disjunction(conditions);
        }
        return condition;
    }

    public Condition parseCondition() {
        return parseDisjunction();
    }

    public Rule parseRule() {
        clearVariables();
        Predicate predicate = parsePredicate();
        if (tokenizer.peekToken().getType() == TokenType.IMPLICATION) {
            tokenizer.consume(TokenType.IMPLICATION);
            Condition condition = parseCondition();
            tokenizer.consume(TokenType.DOT);
            return new Rule(predicate, condition);
        } else {
            tokenizer.consume(TokenType.DOT);
            return new Rule(predicate);
        }
    }

}
