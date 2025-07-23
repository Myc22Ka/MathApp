package pl.myc22ka.mathapp.model.set.utils.token;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a single token in a set expression — can be an operand, operator, or parenthesis.
 *
 * @param type       the type of the token (e.g., operand, operator, left/right parenthesis)
 * @param value      the string value of the token (e.g., "A", "∪", "{1,2}")
 * @param precedence the precedence of the operator (only applies if the token is an operator)
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
public record Token(TokenType type, String value, int precedence) {

    @Override
    public @NotNull String toString() {
        return String.format("Token{type=%s, value='%s', precedence=%d}", type, value, precedence);
    }
}
