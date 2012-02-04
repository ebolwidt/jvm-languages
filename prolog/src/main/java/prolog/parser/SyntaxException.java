package prolog.parser;

public class SyntaxException extends RuntimeException {

    private Token token;

    public SyntaxException(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
