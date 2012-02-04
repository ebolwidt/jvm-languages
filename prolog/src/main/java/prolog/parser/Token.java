package prolog.parser;

public class Token {
    private TokenType type;
    private String text;

    public Token(TokenType type, String text) {
        if (type == null)
            throw new NullPointerException("type");
        if (text == null)
            throw new NullPointerException("text");
        this.type = type;
        this.text = text;
    }

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass())
            return false;
        Token other = (Token) o;
        return type == other.type && text.equals(other.text);
    }

    @Override
    public int hashCode() {
        return type.hashCode() ^ text.hashCode();
    }

    @Override
    public String toString() {
        return "Token(" + type + ", " + text + ")";
    }
}
