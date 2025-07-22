package pl.myc22ka.mathapp.model.set;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.parsers.SetParser;
import pl.myc22ka.mathapp.model.set.sets.*;

import java.util.List;

import static pl.myc22ka.mathapp.model.set.ISetType.FUNDAMENTAL;
import static pl.myc22ka.mathapp.model.set.ISetType.INTERVAL;
import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;
import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

/**
 * Factory class for creating {@link ISet} instances from string expressions.
 * Supports finite sets, intervals, and fundamental sets.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 2025‑06‑19
 */
public class SetFactory {

    /**
     * Parses a set expression and returns the corresponding {@link ISet} implementation.
     *
     * @param setExpression the string representation of the set
     * @return the corresponding {@link ISet} instance
     * @throws IllegalArgumentException if the expression is unsupported
     */
    public static @NotNull ISet fromString(@NotNull String setExpression) {
        String trimmed = setExpression.replaceAll("\\s+", "");

        if (SetSymbols.isReal(trimmed)) return new Fundamental(REAL);

        var binaryOperations = SetSymbols.getBinaryOperations();

        boolean containsFundamental = SetSymbols.getSymbols().stream()
                .anyMatch(symbol -> trimmed.contains(symbol.toString()));

        boolean containsBinaryOps = binaryOperations.stream()
                .anyMatch(op -> trimmed.contains(op.toString()));

        if (containsBinaryOps && !containsFundamental) {
            return SetParser.parse(trimmed);
        }

        return parseSimpleExpression(trimmed, binaryOperations);
    }

    /**
     * Parses simple expressions without binary operations.
     */
    private static @NotNull ISet parseSimpleExpression(@NotNull String trimmed,
                                                       @NotNull List<SetSymbols> binaryOperations) {
        // 1. Check for finite set (e.g. {1,2,3})
        if (isFiniteSet(trimmed, binaryOperations)) {
            return new Finite(trimmed);
        }

        // 2. Check for intervals (e.g. [1,5], (2,10], [sqrt(10), 10))
        if (isInterval(trimmed)) {
            String symjaExpr = convertToIntervalExpression(trimmed);

            return new Interval(symjaExpr);
        }

        // 3. Stand-alone fundamental symbol ℝ, ℕ, ∅
        if (SetSymbols.equals(trimmed)) {
            return new Fundamental(trimmed);
        }

        // 4. Check for complex expressions with multiple operators
        if (containsMultipleOperators(trimmed, binaryOperations)) {
            return SetParser.parse(trimmed);
        }

        // 5. Reduced fundamental expressions like ℝ\{1,2} (single operator case)
        return parseReducedFundamental(trimmed, binaryOperations);
    }

    /**
     * Parses reduced fundamental expressions (e.g., ℝ\{1,2}).
     */
    private static @NotNull ISet parseReducedFundamental(@NotNull String trimmed,
                                                         @NotNull List<SetSymbols> binaryOperations) {
        for (SetSymbols symbol : binaryOperations) {
            String operator = symbol.toString();
            int position = findOperatorPosition(trimmed, operator);

            if (position <= 0) continue;

            ISet left = fromString(trimmed.substring(0, position));
            String rightExpr = trimmed.substring(position + operator.length());

            rightExpr = stripOuterParentheses(rightExpr);

            ISet right = containsMultipleOperators(rightExpr, binaryOperations)
                    ? SetParser.parse(rightExpr)
                    : fromString(rightExpr);

            if (left.getISetType() == FUNDAMENTAL || left.getISetType() == INTERVAL) {
                return new ReducedFundamental(left, symbol, right);
            }
        }

        return new Fundamental(EMPTY);
    }

    /**
     * Removes outer parentheses if they encompass the entire expression.
     */
    private static @NotNull String stripOuterParentheses(@NotNull String expr) {
        if (!expr.startsWith("(") || !expr.endsWith(")")) {
            return expr;
        }

        if (hasMatchingOuterParentheses(expr)) {
            return expr.substring(1, expr.length() - 1);
        }

        return expr;
    }

    /**
     * Checks if outer parentheses match and encompass the entire expression.
     */
    private static boolean hasMatchingOuterParentheses(@NotNull String expr) {
        int depth = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;

            // If depth becomes 0 before the last character, outer parentheses don't encompass all
            if (depth == 0 && i < expr.length() - 1) {
                return false;
            }
        }
        return depth == 0;
    }

    /**
     * Checks if the expression contains multiple binary operators at the top level.
     */
    private static boolean containsMultipleOperators(@NotNull String expression,
                                                     @NotNull List<SetSymbols> binaryOperations) {
        int operatorCount = 0;
        DepthTracker depth = new DepthTracker();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            depth.updateDepth(c);

            if (depth.isAtTopLevel()) {
                for (SetSymbols symbol : binaryOperations) {
                    String operator = symbol.toString();
                    if (expression.startsWith(operator, i)) {
                        operatorCount++;
                        if (operatorCount > 1) {
                            return true;
                        }
                        i += operator.length() - 1;
                        break;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Finds the position of an operator, considering nesting levels.
     */
    private static int findOperatorPosition(@NotNull String expression, @NotNull String operator) {
        DepthTracker depth = new DepthTracker();

        for (int i = 0; i <= expression.length() - operator.length(); i++) {
            char c = expression.charAt(i);
            depth.updateDepth(c);

            if (depth.isAtTopLevel() && expression.startsWith(operator, i)) {
                return i;
            }
        }

        return -1; // Operator not found at top level
    }

    /**
     * Checks if the expression represents a finite set.
     */
    private static boolean isFiniteSet(@NotNull String expr, @NotNull List<SetSymbols> operations) {
        if (!expr.matches("\\{.*}")) {
            return false;
        }

        return operations.stream().noneMatch(op -> expr.contains(op.toString()));
    }

    /**
     * Checks if the expression represents an interval.
     * Supports formats like [1, 5], (2, 10], [sqrt(10), 10), etc.
     */
    private static boolean isInterval(@NotNull String expr) {
        return expr.matches("^\\s*[\\[(].*[])]\\s*$");
    }

    /**
     * Converts interval notation to internal expression format.
     */
    private static @NotNull String convertToIntervalExpression(@NotNull String expression) {
        String trimmed = expression.trim();

        if (!isInterval(trimmed)) {
            throw new IllegalArgumentException("Invalid interval format: " + expression);
        }

        char leftBracket = trimmed.charAt(0);
        char rightBracket = trimmed.charAt(trimmed.length() - 1);

        BoundType leftBound = (leftBracket == '[') ? BoundType.CLOSED : BoundType.OPEN;
        BoundType rightBound = (rightBracket == ']') ? BoundType.CLOSED : BoundType.OPEN;

        String content = trimmed.substring(1, trimmed.length() - 1).trim();
        int commaPosition = findTopLevelComma(content);

        if (commaPosition == -1) {
            throw new IllegalArgumentException("Invalid interval format: " + expression);
        }

        String start = content.substring(0, commaPosition).trim();
        String end = content.substring(commaPosition + 1).trim();

        return String.format("IntervalData({%s, %s, %s, %s})", start, leftBound, rightBound, end);
    }

    /**
     * Finds the position of a comma at the top level (not inside parentheses).
     */
    private static int findTopLevelComma(@NotNull String content) {
        int parenthesesDepth = 0;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '(') {
                parenthesesDepth++;
            } else if (c == ')') {
                parenthesesDepth--;
            } else if (c == ',' && parenthesesDepth == 0) {
                return i;
            }
        }

        return -1; // No top-level comma found
    }

    /**
     * Helper class to track nesting depth in expressions.
     */
    private static class DepthTracker {
        private int braceDepth = 0;
        private int parenthesesDepth = 0;
        private int bracketDepth = 0;

        void updateDepth(char c) {
            switch (c) {
                case '{' -> braceDepth++;
                case '}' -> braceDepth--;
                case '(' -> parenthesesDepth++;
                case ')' -> parenthesesDepth--;
                case '[' -> bracketDepth++;
                case ']' -> bracketDepth--;
            }
        }

        boolean isAtTopLevel() {
            return braceDepth == 0 && parenthesesDepth == 0 && bracketDepth == 0;
        }
    }
}