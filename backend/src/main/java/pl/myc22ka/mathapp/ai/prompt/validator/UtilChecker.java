package pl.myc22ka.mathapp.ai.prompt.validator;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.set.SetSymbols;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Util checker.
 * <p>
 * Provides utility methods for analyzing and validating mathematical expressions
 * represented as strings, including detection of fractions, roots, set operations,
 * and complexity checks.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Component
@RequiredArgsConstructor
public class UtilChecker {

    private static final Pattern FRACTION_PATTERN = Pattern.compile("\\b-?\\d+/\\d+\\b");
    private static final Pattern ROOT_PATTERN = Pattern.compile("(√\\s*\\(?[\\w\\d+\\-*/^ ]+\\)?|sqrt\\s*\\(.*?\\))");

    /**
     * Checks if the input text contains any fraction.
     *
     * @param text the text to check
     * @return true if a fraction is found, false otherwise
     */
    public boolean containsFraction(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }

        Matcher matcher = FRACTION_PATTERN.matcher(text);
        return matcher.find();
    }

    /**
     * Checks if the input text contains any root expression.
     *
     * @param text the text to check
     * @return true if a root is found, false otherwise
     */
    public boolean containsRoot(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        return ROOT_PATTERN.matcher(text).find();
    }

    /**
     * Checks if the input text contains only fractions, ignoring whitespace
     * and common arithmetic operators or brackets.
     *
     * @param text the text to check
     * @return true if the text contains only fractions, false otherwise
     */
    public boolean containsOnlyFractions(String text) {
        if (text == null || text.isBlank()) return false;

        // (), [], {}, + - * / and spaces
        String cleaned = text.replaceAll("[\\s+\\-*/()\\[\\]{}]", "");

        if (!cleaned.matches("([0-9]+/[0-9]+)*")) {
            return false;
        }

        return FRACTION_PATTERN.matcher(text).find();
    }

    /**
     * Checks if the input text contains only root expressions,
     * ignoring whitespace and common operators or brackets.
     *
     * @param text the text to check
     * @return true if the text contains only roots, false otherwise
     */
    public boolean containsOnlyRoots(String text) {
        if (text == null || text.isBlank()) return false;

        String cleaned = text.replaceAll("[\\s+\\-*/()\\[\\]{}]", "");

        String[] tokens = cleaned.split("(?=√)");

        for (String token : tokens) {
            if (token.isBlank()) continue;
            if (!ROOT_PATTERN.matcher(token).matches()) {
                return false;
            }
        }

        return ROOT_PATTERN.matcher(text).find();
    }

    /**
     * Checks if the given string contains any set operation symbols,
     * as defined by the binary set operations in SetSymbols.
     *
     * @param str the string to check
     * @return true if any set operation symbol is found, false otherwise
     */
    public boolean containsSetOperations(@NotNull String str) {
        String operations = SetSymbols.getBinaryOperationsString();
        for (char c : str.toCharArray()) {
            if (operations.indexOf(c) != -1) return true;
        }
        return false;
    }

    /**
     * Checks if the input string has mixed complexity, defined as containing brackets
     * along with set operations, fractions, or roots.
     *
     * @param str the string to check
     * @return true if mixed complexity is detected, false otherwise
     */
    public boolean hasMixedComplexity(@NotNull String str) {
        return str.matches(".*[()\\[\\]{}].*") &&
                (containsSetOperations(str) || containsFraction(str) || containsRoot(str));
    }
}
