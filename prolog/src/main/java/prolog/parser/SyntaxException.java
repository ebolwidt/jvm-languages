package prolog.parser;

public class SyntaxException extends RuntimeException {

    private Token token;

    public SyntaxException(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return super.toString() + (token == null ? "" : ": " + token.toString());
    }
}
