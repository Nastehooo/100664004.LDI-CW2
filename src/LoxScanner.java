import java.util.ArrayList;
import java.util.List;

public class LoxScanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;

    public LoxScanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null)); // End-of-file token
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '+': addToken(TokenType.PLUS); break;
            case '-': addToken(TokenType.MINUS); break;
            case '*': addToken(TokenType.STAR); break;
            case '/': addToken(TokenType.SLASH); break;
            case ' ':
            case '\r':
            case '\t':
            case '\n':
                // Ignore whitespace
                break;
            default:
                if (Character.isDigit(c)) {
                    number();
                } else {
                    throw new RuntimeException("Unexpected character: " + c);
                }
        }
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        tokens.add(new Token(type, source.substring(start, current), null));
    }

    private void number() {
        while (!isAtEnd() && Character.isDigit(peek())) advance();

        // Handle decimal numbers
        if (peek() == '.' && isDigit(peekNext())) {
            advance(); // Consume '.'

            while (!isAtEnd() && Character.isDigit(peek())) advance();
        }

        String numberText = source.substring(start, current);
        tokens.add(new Token(TokenType.NUMBER, numberText, Double.parseDouble(numberText)));
    }

    private char peek() {
        return isAtEnd() ? '\0' : source.charAt(current);
    }

    private char peekNext() {
        return (current + 1 >= source.length()) ? '\0' : source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
