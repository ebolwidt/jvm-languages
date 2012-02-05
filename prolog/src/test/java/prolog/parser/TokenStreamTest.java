package prolog.parser;

import junit.framework.TestCase;

public class TokenStreamTest extends TestCase {
    public void testConsume() {
        String program = "a, b, c";
        Tokenizer tokenizer = new Tokenizer(program);
        TokenStream stream = new TokenStream(tokenizer);

        stream.consume(TokenType.IDENTIFIER); // Consuming a
        try {
            // Bad token type - the next token is a COMMA
            stream.consume(TokenType.CUT);
            fail("Consume allowed the consumption of a token that didn't match the token-type");
        } catch (IllegalStateException e) {
            // OK
        }
    }
}
