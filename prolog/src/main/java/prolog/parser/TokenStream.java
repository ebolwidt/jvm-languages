package prolog.parser;

public class TokenStream {
    private Tokenizer tokenizer;

    private Token pushedBack;

    public TokenStream(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void consume(TokenType type) {
        Token token = nextToken();
        if (token.getType() != type) {
            throw new IllegalStateException("Type mismatch on token consume, expected " + type + " but received "
                    + token.getType());
        }
    }

    public Token nextToken() {
        if (pushedBack != null) {
            Token result = pushedBack;
            pushedBack = null;
            return result;
        } else {
            return tokenizer.nextToken();
        }
    }

    public Token peekToken() {
        pushedBack = nextToken();
        return pushedBack;
    }

    // public void pushBack(Token token) {
    // if (pushedBack != null) {
    // throw new IllegalStateException("There is already a pushed back token, only one push back is supported");
    // }
    // pushedBack = token;
    // }

}
