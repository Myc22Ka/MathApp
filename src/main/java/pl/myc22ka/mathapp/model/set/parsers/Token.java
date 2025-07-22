package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;

/**
 * Token class for representing elements of the expression
 */
public record Token(TokenType type, String value, int precedence) {

    @Override
    public @NotNull String toString() {
        return String.format("Token{type=%s, value='%s', precedence=%d}", type, value, precedence);
    }
}
