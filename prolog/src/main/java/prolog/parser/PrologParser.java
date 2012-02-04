package prolog.parser;

import java.util.ArrayList;
import java.util.List;

import prolog.PredicateKey;
import prolog.expression.Constant;
import prolog.expression.Expression;
import prolog.expression.Predicate;
import prolog.expression.Variable;

public class PrologParser {

    private TokenStream tokenizer;

    public PrologParser(String program) {
        this.tokenizer = new TokenStream(new Tokenizer(program));
    }

    public PrologParser(Tokenizer tokenizer) {
        this.tokenizer = new TokenStream(tokenizer);
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
            return new Variable(token.getText());
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
            else if (peek.getType() == TokenType.RPAREN)
                break;
        }
        return new Predicate(new PredicateKey(token.getText(), arguments.size()), arguments.toArray(new Expression[arguments
                .size()]));
    }

    // The kind of expression that can appear in a predicate argument
    public Expression parseExpression() {
        Token token = tokenizer.nextToken();
        switch (token.getType()) {
        case IDENTIFIER:
            // For this method, zero-arity predicates are also considered atoms.
            Token next = tokenizer.peekToken();
            if (next.getType() == TokenType.LPAREN)
                return parsePredicate(token);
        default:
            return parseAtom(token);
        }
    }

}
