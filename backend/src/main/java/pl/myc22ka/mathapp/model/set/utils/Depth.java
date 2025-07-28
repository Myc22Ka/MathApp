package pl.myc22ka.mathapp.model.set.utils;

/**
 * Utility class to track the nesting depth of different types of brackets:
 * braces '{}', parentheses '()', and square brackets '[]'.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
public class Depth {
    int braces = 0, parens = 0, brackets = 0;

    /**
     * Updates the depth counters based on the given character.
     *
     * @param c the character to process (expected to be one of '{', '}', '(', ')', '[', ']')
     */
    public void update(char c) {
        switch (c) {
            case '{' -> braces++;
            case '}' -> braces--;
            case '(' -> parens++;
            case ')' -> parens--;
            case '[' -> brackets++;
            case ']' -> brackets--;
        }
    }

    /**
     * Checks if all tracked bracket depths are zero,
     * indicating that the current position is at the "top level"
     * with no unmatched opening brackets.
     *
     * @return true if no unmatched brackets are open; false otherwise
     */
    public boolean isTop() {
        return braces == 0 && parens == 0 && brackets == 0;
    }
}
