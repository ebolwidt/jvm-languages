package prolog.parser;

import junit.framework.TestCase;

public class Tokenizer001Test extends TestCase {
    public void testTokensSimpleFact() {
        // Basic sanity checker
        String program = "father('Piet', \"Jan\").";
        Tokenizer tokenizer = new Tokenizer(program);

        assertEquals(new Token(TokenType.IDENTIFIER, "father"), tokenizer.nextToken());
        assertEquals(new Token(TokenType.LPAREN, "("), tokenizer.nextToken());
        assertEquals(new Token(TokenType.STRING, "Piet"), tokenizer.nextToken());
        assertEquals(new Token(TokenType.COMMA, ","), tokenizer.nextToken());
        assertEquals(new Token(TokenType.STRING, "Jan"), tokenizer.nextToken());
        assertEquals(new Token(TokenType.RPAREN, ")"), tokenizer.nextToken());
        assertEquals(TokenType.DOT, tokenizer.nextToken().getType());
        assertEquals(TokenType.END, tokenizer.nextToken().getType());
    }

    public void testTokensInvalid() {
        String program = "`";
        Tokenizer tokenizer = new Tokenizer(program);

        assertEquals(TokenType.INVALID, tokenizer.nextToken().getType());
        assertEquals(TokenType.END, tokenizer.nextToken().getType());
    }

    public void testTokensNoWhitespace() {
        String program = ":-,";
        Tokenizer tokenizer = new Tokenizer(program);

        assertEquals(TokenType.IMPLICATION, tokenizer.nextToken().getType());
        assertEquals(TokenType.COMMA, tokenizer.nextToken().getType());
        assertEquals(TokenType.END, tokenizer.nextToken().getType());
    }

    public void testTokensOperators() {
        String program = "( ) [ ] [] , ; . :- | ! + -";
        Tokenizer tokenizer = new Tokenizer(program);

        assertEquals(TokenType.LPAREN, tokenizer.nextToken().getType());
        assertEquals(TokenType.RPAREN, tokenizer.nextToken().getType());
        assertEquals(TokenType.LBRACKET, tokenizer.nextToken().getType());
        assertEquals(TokenType.RBRACKET, tokenizer.nextToken().getType());
        assertEquals(TokenType.EMPTY_LIST, tokenizer.nextToken().getType());
        assertEquals(TokenType.COMMA, tokenizer.nextToken().getType());
        assertEquals(TokenType.SEMICOLON, tokenizer.nextToken().getType());
        assertEquals(TokenType.DOT, tokenizer.nextToken().getType());
        assertEquals(TokenType.IMPLICATION, tokenizer.nextToken().getType());
        assertEquals(TokenType.PIPE, tokenizer.nextToken().getType());
        assertEquals(TokenType.CUT, tokenizer.nextToken().getType());
        assertEquals(TokenType.PLUS, tokenizer.nextToken().getType());
        assertEquals(TokenType.MINUS, tokenizer.nextToken().getType());
        assertEquals(TokenType.END, tokenizer.nextToken().getType());
    }

    public void testNumbers() {
        String program = "42 -328 999999999999";
        Tokenizer tokenizer = new Tokenizer(program);

        assertEquals(new Token(TokenType.NUMBER, "42"), tokenizer.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "-328"), tokenizer.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "999999999999"), tokenizer.nextToken());
    }

    public void testVariables() {
        String program = "X Member _ _Member";
        Tokenizer tokenizer = new Tokenizer(program);

        assertEquals(new Token(TokenType.VARIABLE, "X"), tokenizer.nextToken());
        assertEquals(new Token(TokenType.VARIABLE, "Member"), tokenizer.nextToken());
        assertEquals(new Token(TokenType.VARIABLE, "_"), tokenizer.nextToken());
        assertEquals(new Token(TokenType.VARIABLE, "_Member"), tokenizer.nextToken());
    }
}
