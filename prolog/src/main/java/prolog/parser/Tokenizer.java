package prolog.parser;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

public class Tokenizer {
    private static class RuntimeIOException extends RuntimeException {
        public RuntimeIOException(IOException e) {
            super(e);
        }

        public RuntimeIOException() {
        }
    }

    private static class RuntimeEOFException extends RuntimeIOException {
    }

    static class OperatorDefinition {
        private String token;
        private TokenType type;

        OperatorDefinition(String token, TokenType type) {
            this.token = token;
            this.type = type;
        }
    }

    private static OperatorDefinition[] OPERATORS_BY_LENGTH_DESC = {
    /** */
    new OperatorDefinition(":-", TokenType.IMPLICATION),
    /** */
    new OperatorDefinition("[]", TokenType.EMPTY_LIST),
    /** */
    new OperatorDefinition("[", TokenType.LBRACKET),
    /** */
    new OperatorDefinition("]", TokenType.RBRACKET),
    /** */
    new OperatorDefinition("(", TokenType.LPAREN),
    /** */
    new OperatorDefinition(")", TokenType.RPAREN),
    /** */
    new OperatorDefinition(",", TokenType.COMMA),
    /** */
    new OperatorDefinition(";", TokenType.SEMICOLON),
    /** */
    new OperatorDefinition("!", TokenType.CUT),
    /** */
    new OperatorDefinition("|", TokenType.PIPE),
    /** */
    new OperatorDefinition(".", TokenType.DOT),
    /** */
    new OperatorDefinition("+", TokenType.PLUS),
    /** */
    new OperatorDefinition("-", TokenType.MINUS),
    /** */
    new OperatorDefinition("=", TokenType.EQUALS),
    /** */
    };

    private PushbackReader in;

    private boolean eof;

    public Tokenizer(String data) {
        this(new StringReader(data));
    }

    public Tokenizer(Reader r) {
        this.in = new PushbackReader(r);
    }

    public Token nextToken() {
        char ch = readNonWhitespace();
        if (eof)
            return new Token(TokenType.END, "");

        if (isNumberStart(ch)) {
            return readNumber(ch);
        } else if (isOperatorStart(ch)) {
            return readOperator(ch);
        } else if (isIdentifierStart(ch)) {
            return readIdentifier(ch);
        } else if (isStringStart(ch)) {
            return readString(ch);
        } else {
            return new Token(TokenType.INVALID, String.valueOf(ch));
        }
    }

    private static boolean isOperatorStart(char ch) {
        for (OperatorDefinition definition : OPERATORS_BY_LENGTH_DESC) {
            if (ch == definition.token.charAt(0))
                return true;
        }
        return false;
    }

    private Token readOperator(char ch) {
        StringBuilder b = new StringBuilder();
        b.append(ch);

        String operator;
        do {
            ch = readEofAllowed();
            if (eof)
                break;
            b.append(ch);
            operator = b.toString();
        } while (hasOperatorMatch(operator));
        if (!eof) {
            unread(ch);
            b.setLength(b.length() - 1);
        }
        operator = b.toString();
        return new Token(operatorTokenType(operator), operator);
    }

    private static boolean hasOperatorMatch(String test) {
        for (OperatorDefinition definition : OPERATORS_BY_LENGTH_DESC) {
            if (definition.token.startsWith(test))
                return true;
        }
        return false;
    }

    private TokenType operatorTokenType(String test) {
        for (OperatorDefinition definition : OPERATORS_BY_LENGTH_DESC) {
            if (definition.token.equals(test))
                return definition.type;
        }
        return TokenType.INVALID;
    }

    private boolean isNumberStart(char ch) {
        if (ch >= '0' && ch <= '9')
            return true;
        if (ch == '-') {
            ch = readEofAllowed();
            unread(ch);
            if (eof || ch < '0' || ch > '9') {
                return false;
            }
            return true;
        }
        return false;
    }

    private Token readNumber(char ch) {
        StringBuilder b = new StringBuilder();
        b.append(ch);
        while ((ch = readEofAllowed()) >= '0' && ch <= '9') {
            b.append(ch);
        }
        unread(ch);
        return new Token(TokenType.NUMBER, b.toString());
    }

    private static boolean isIdentifierStart(char ch) {
        return Character.isJavaIdentifierStart(ch);
    }

    private Token readIdentifier(char startChar) {
        StringBuilder b = new StringBuilder();
        b.append(startChar);
        char ch = '\0';
        while (Character.isJavaIdentifierPart(ch = readEofAllowed()) && !eof) {
            b.append(ch);
        }
        unread(ch);
        if (startChar == '_' || (startChar >= 'A' && startChar <= 'Z'))
            return new Token(TokenType.VARIABLE, b.toString());
        else
            return new Token(TokenType.IDENTIFIER, b.toString());
    }

    private static boolean isStringStart(char ch) {
        return ch == '"' || ch == '\'';
    }

    private Token readString(char startChar) {
        StringBuilder b = new StringBuilder();
        char ch;
        while ((ch = read()) != startChar) {
            b.append(ch);
        }
        return new Token(TokenType.STRING, b.toString());
    }

    private char readNonWhitespace() {
        if (eof)
            return '\0';
        char ch;
        do {
            ch = readEofAllowed();
        } while (!eof && Character.isWhitespace(ch));
        return ch;
    }

    private void unread(char ch) {
        if (ch == '\0')
            return;
        try {
            in.unread(ch);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    private char readEofAllowed() {
        try {
            int ch = in.read();
            if (ch < 0) {
                eof = true;
                return '\0';
            }
            return (char) ch;
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    private char read() {
        char ch = readEofAllowed();
        if (eof)
            throw new RuntimeEOFException();
        return ch;
    }
}
