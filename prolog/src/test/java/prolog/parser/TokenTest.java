package prolog.parser;

import junit.framework.TestCase;

public class TokenTest extends TestCase {

    public void testToString() {
        assertEquals("Token(STRING, FooBar)", new Token(TokenType.STRING, "FooBar").toString());
    }

    public void testHashCode() {
        Token a = new Token(TokenType.STRING, "FooBar");
        Token b = new Token(TokenType.STRING, "FooBar");
        Token c = new Token(TokenType.IDENTIFIER, "FooBar");
        Token d = new Token(TokenType.STRING, "BarBaz");

        assertEquals(a.hashCode(), b.hashCode());
        // This is not part of the contract of hashCode; hashCode could return the
        // same value of everything.
        // However I want the hashCode to be different whenever either the type is different,
        // or the hashCode of the text is different.
        assertFalse(a.hashCode() == c.hashCode());
        assertFalse(a.hashCode() == d.hashCode());
    }

    public void testEqualsCode() {
        Token a = new Token(TokenType.STRING, "FooBar");
        Token b = new Token(TokenType.STRING, "FooBar");
        Token c = new Token(TokenType.IDENTIFIER, "FooBar");
        Token d = new Token(TokenType.STRING, "BarBaz");

        assertEquals(a, b);
        assertEquals(b, a);
        assertFalse(a.equals(c));
        assertFalse(c.equals(a));
        assertFalse(a.equals(d));
        assertFalse(d.equals(a));

        assertFalse(a.equals(null));
        assertFalse(a.equals(new Integer(42)));
    }

    public void testFailEarlyConstructor() {
        // I want the constructor to fail if type or text is null,
        // to catch these types of problems as early as possible.
        try {
            new Token(null, "");
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("type", e.getMessage());
        }

        try {
            new Token(TokenType.INVALID, null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("text", e.getMessage());
        }
    }

    public void testConstructorIsntMoronic() {
        // Types are always possible, regression test:

        Token t = new Token(TokenType.IDENTIFIER, "foobar");
        assertEquals(TokenType.IDENTIFIER, t.getType());
        assertEquals("foobar", t.getText());

    }
}
