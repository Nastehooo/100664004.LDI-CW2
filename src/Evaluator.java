import java.util.List;


public class Evaluator {
    private final List<Token> tokens;
    private int current = 0;

    public Evaluator(List<Token> tokens) {
        this.tokens = tokens;
    }

    public double evaluate() {
        return expression();
    }

    private double expression() {
        return term();
    }

    private double term() {
        double result = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            double right = factor();
            if (operator.type == TokenType.PLUS) {
                result += right;
            } else {
                result -= right;
            }
        }
        return result;
    }

    private double factor() {
        double result = unary();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            double right = unary();
            if (operator.type == TokenType.STAR) {
                result *= right;
            } else {
                if (right == 0) {
                    throw new RuntimeException("Division by zero is not allowed.");
                }
                result /= right;
            }
        }
        return result;
    }

    private double unary() {
        if (match(TokenType.MINUS)) {
            return -unary();
        }
        return primary();
    }

    private double primary() {
        if (match(TokenType.NUMBER)) {
            return (double) previous().literal;
        }

        if (match(TokenType.LEFT_PAREN)) {
            double expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return expr;
        }

        throw new RuntimeException("Unexpected token: " + peek().type);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String errorMessage) {
        if (check(type)) return advance();
        throw new RuntimeException(errorMessage);
    }
}
