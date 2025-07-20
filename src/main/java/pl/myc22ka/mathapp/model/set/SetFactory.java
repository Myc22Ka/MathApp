package pl.myc22ka.mathapp.model.set;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.parsers.SetExpressionParser;
import pl.myc22ka.mathapp.model.set.sets.Finite;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.sets.ReducedFundamental;

import java.util.List;

/**
 * Factory class for creating {@link ISet} instances from string expressions.
 * Supports finite sets, intervals, and fundamental sets.
 *
 * @author Myc22Ka
 * @version 1.0
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
        return fromString(setExpression, false);
    }

    private static @NotNull ISet fromString(@NotNull String setExpression, boolean isParenthesized) {
        var binaryOperations = SetSymbols.getBinaryOperations();

        String trimmed = setExpression.replaceAll("\\s+", "");

        if (trimmed.isEmpty()) {
            return new Fundamental(SetSymbols.EMPTY.toString());
        }

        if(trimmed.equals("(-∞,∞)") || trimmed.equals("(-∞,+∞)")) {
            return new Fundamental(SetSymbols.REAL);
        }

        boolean containsFundamental = SetSymbols.getSymbols().stream()
                .anyMatch(op -> trimmed.contains(op.toString()));

        boolean containsBinaryOps = binaryOperations.stream()
                .anyMatch(op -> trimmed.contains(op.toString()));

        if (containsBinaryOps && !containsFundamental) {
            return SetExpressionParser.parseAndEvaluate(trimmed);
        }

        String stripped = stripOuterParentheses(trimmed, containsBinaryOps);
        if (!stripped.equals(trimmed)) {
            // Jeśli usunęliśmy nawiasy, ustaw flagę
            return fromString(stripped, true);
        }

        return parseSimpleExpression(trimmed, binaryOperations, isParenthesized);
    }

    /**
     * Parses simple expressions without binary operations
     */
    private static @NotNull ISet parseSimpleExpression(@NotNull String trimmed,
                                                       @NotNull List<SetSymbols> binaryOperations,
                                                       boolean isParenthesized) {
        // 1. Check for finite set (e.g. {1,2,3})
        if (isFinite(trimmed, binaryOperations)) return new Finite(trimmed);

        // 2. Check for closed or open intervals (e.g. [1,5], (2,10], [sqrt(10), 10), etc.)
        if (isInterval(trimmed)) return new Interval(trimmed);

        // 3. Stand-alone fundamental symbol ℝ, ℕ, ∅ ...
        if (SetSymbols.equals(trimmed)) return new Fundamental(trimmed);

        // 4. Check if this is a complex expression with multiple operators
        if (containsMultipleOperators(trimmed, binaryOperations)) {
            // Use the expression parser for complex expressions
            return SetExpressionParser.parseAndEvaluate(trimmed);
        }

        // 5. NEW CASE: Check for expressions like (ℝ∖ℤ)∖[1,6] where we have a parenthesized
        //    complex expression followed by an operator and another operand
        if (trimmed.startsWith("(") && !isInterval(trimmed)) {
            int closingParen = findMatchingClosingParen(trimmed);
            if (closingParen > 0 && closingParen < trimmed.length() - 1) {
                // There's something after the closing parenthesis
                String afterParen = trimmed.substring(closingParen + 1);

                // Check if what follows is a binary operator
                for (SetSymbols symbol : binaryOperations) {
                    String op = symbol.toString();
                    if (afterParen.startsWith(op)) {
                        String leftExpr = trimmed.substring(1, closingParen); // Remove outer parentheses
                        String rightExpr = afterParen.substring(op.length());

                        // Parse left side using expression parser (it contains operators)
                        ISet left = SetExpressionParser.parseAndEvaluate(leftExpr);

                        // Parse right side normally
                        ISet right = fromString(rightExpr);

                        // Apply the operation
                        return switch (symbol) {
                            case UNION -> left.union(right);
                            case INTERSECTION -> left.intersection(right);
                            case DIFFERENCE -> left.difference(right);
                            default -> throw new IllegalArgumentException("Unsupported operator: " + symbol);
                        };
                    }
                }
            }
        }

        // 6. Reduced fundamental ℝ\{1,2} ... (single operator case)
        for (SetSymbols symbol : SetSymbols.getBinaryOperations()) {
            String rep = symbol.toString();
            int pos = findOperatorPosition(trimmed, rep);
            if (pos <= 0) continue;

            ISet left = fromString(trimmed.substring(0, pos));
            String rightExpr = trimmed.substring(pos + rep.length());

            // Usuwamy nawiasy i ustawiamy flagę, ale przekazujemy ją dalej
            boolean hadOuterParens = false;
            String strippedRightExpr = stripOuterParentheses(rightExpr, false);
            if (!strippedRightExpr.equals(rightExpr)) {
                hadOuterParens = true;
                rightExpr = strippedRightExpr;
            }

            ISet right = containsMultipleOperators(rightExpr, binaryOperations)
                    ? SetExpressionParser.parseAndEvaluate(rightExpr)
                    : fromString(rightExpr, hadOuterParens);

            if (left.getISetType() == ISetType.FUNDAMENTAL || left.getISetType() == ISetType.INTERVAL) {
                var result = new ReducedFundamental(left, symbol, right, isParenthesized || hadOuterParens);

                return result.simplify().toInterval();
            }
        }
        return new Fundamental(SetSymbols.EMPTY.toString());
    }

    /**
     * Helper method to find the matching closing parenthesis for a given opening parenthesis
     */
    private static int findMatchingClosingParen(String expr) {
        if (expr.isEmpty() || expr.charAt(0) != '(') {
            return -1;
        }

        int depth = 1;
        for (int i = 1; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1; // No matching closing parenthesis found
    }

    private static String stripOuterParentheses(String expr, boolean containsBinaryOps) {
        if (expr.startsWith("(") && expr.endsWith(")") && containsBinaryOps) {
            // Sprawdź, czy nawiasy zewnętrzne są parzysto zagnieżdżone
            int depth = 0;
            for (int i = 0; i < expr.length(); i++) {
                char c = expr.charAt(i);
                if (c == '(') depth++;
                else if (c == ')') depth--;
                // Jeśli przedostatni znak zamyka pierwszego nawiasu to znaczy, że zewnętrzne nawiasy ok
                if (depth == 0 && i < expr.length() - 1) {
                    return expr; // Nawiasy nie obejmują całego wyrażenia
                }
            }
            // Jeśli doszliśmy do końca i depth == 0, to możemy usunąć nawiasy
            return expr.substring(1, expr.length() - 1);
        }
        return expr;
    }

    /**
     * Checks if the expression contains multiple binary operators at the top level
     */
    private static boolean containsMultipleOperators(@NotNull String expression,
                                                     @NotNull List<SetSymbols> binaryOperations) {
        int operatorCount = 0;
        int i = 0;
        int braceDepth = 0;
        int parenDepth = 0;
        int bracketDepth = 0;

        while (i < expression.length()) {
            char c = expression.charAt(i);

            // Track nesting depth
            if (c == '{') braceDepth++;
            else if (c == '}') braceDepth--;
            else if (c == '(') parenDepth++;
            else if (c == ')') parenDepth--;
            else if (c == '[') bracketDepth++;
            else if (c == ']') bracketDepth--;

            // Check for operators only at top level (depth 0)
            if (braceDepth == 0 && parenDepth == 0 && bracketDepth == 0) {
                for (SetSymbols symbol : binaryOperations) {
                    String op = symbol.toString();
                    if (expression.startsWith(op, i)) {
                        operatorCount++;
                        if (operatorCount > 1) {
                            return true; // Found multiple operators
                        }
                        i += op.length() - 1; // Skip the operator
                        break;
                    }
                }
            }
            i++;
        }
        return false;
    }

    /**
     * Finds the position of an operator, considering nesting levels
     */
    private static int findOperatorPosition(@NotNull String expression, @NotNull String operator) {
        int i = 0;
        int braceDepth = 0;
        int parenDepth = 0;
        int bracketDepth = 0;

        while (i <= expression.length() - operator.length()) {
            char c = expression.charAt(i);

            // Track nesting depth
            if (c == '{') braceDepth++;
            else if (c == '}') braceDepth--;
            else if (c == '(') parenDepth++;
            else if (c == ')') parenDepth--;
            else if (c == '[') bracketDepth++;
            else if (c == ']') bracketDepth--;

            // Check for operator only at top level (depth 0)
            if (braceDepth == 0 && parenDepth == 0 && bracketDepth == 0) {
                if (expression.startsWith(operator, i)) {
                    return i;
                }
            }
            i++;
        }
        return -1; // Operator not found at top level
    }

    private static boolean isFinite(@NotNull String expr, List<SetSymbols> ops) {
        return expr.matches("\\{.*}") && ops.stream().noneMatch(op -> expr.contains(op.toString()));
    }

    /**
     * Enhanced method to check if expression is an interval, now supporting square roots
     * and other mathematical functions.
     * Examples of supported formats:
     * - [1, 5]
     * - (2, 10]
     * - [sqrt(10), 10)
     * - (sqrt(2), sqrt(8)]
     * - [-sqrt(5), sqrt(5)]
     * - [3, sqrt(10))
     */
    private static boolean isInterval(@NotNull String expr) {
        // Basic structure check: starts with [ or (, ends with ] or )
        if (!expr.matches("[\\[(].*[])]")) {
            return false;
        }

        // Extract the content between brackets/parentheses
        String content = expr.substring(1, expr.length() - 1);

        // Find the comma that separates the two interval bounds
        int commaPos = findTopLevelComma(content);
        if (commaPos == -1) {
            return false; // No comma found at top level
        }

        String leftBound = content.substring(0, commaPos).trim();
        String rightBound = content.substring(commaPos + 1).trim();

        // Check if both bounds are valid mathematical expressions
        boolean leftValid = isValidMathExpression(leftBound);
        boolean rightValid = isValidMathExpression(rightBound);

//        // Debug info (remove in production)
//        if (!leftValid || !rightValid) {
//            System.out.println("DEBUG: leftBound='" + leftBound + "' valid=" + leftValid +
//                    ", rightBound='" + rightBound + "' valid=" + rightValid);
//        }

        return leftValid && rightValid;
    }

    /**
     * Finds the position of a comma at the top level (not inside parentheses)
     */
    private static int findTopLevelComma(@NotNull String content) {
        int parenDepth = 0;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '(') {
                parenDepth++;
            } else if (c == ')') {
                parenDepth--;
            } else if (c == ',' && parenDepth == 0) {
                return i;
            }
        }

        return -1; // No top-level comma found
    }

    /**
     * Checks if a string represents a valid mathematical expression for interval bounds.
     * Supports numbers, negative numbers, infinity symbols, and mathematical functions like sqrt().
     */
    private static boolean isValidMathExpression(@NotNull String expr) {
        if (expr == null || expr.trim().isEmpty()) {
            return false;
        }

        expr = expr.trim(); // Ensure no leading/trailing whitespace

        // Handle infinity symbols
        if (expr.equals("∞") || expr.equals("+∞") || expr.equals("-∞")) {
            return true;
        }

        // Check for simple numbers (integers and decimals, positive and negative)
        // This regex should match: 3, -3, 3.14, -3.14, 0, -0, etc.
        if (expr.matches("-?\\d+(\\.\\d+)?")) {
            return true;
        }

        // Check for mathematical functions
        if (isValidMathFunction(expr)) {
            return true;
        }

        // Check for expressions starting with minus sign followed by a function
        if (expr.startsWith("-") && expr.length() > 1) {
            return isValidMathFunction(expr.substring(1));
        }

        return false;
    }

    /**
     * Validates mathematical functions like sqrt(x), cbrt(x), etc.
     */
    private static boolean isValidMathFunction(@NotNull String expr) {
        // Pattern for mathematical functions: function_name(argument)
        if (!expr.matches("\\w+\\(.+\\)")) {
            return false;
        }

        int parenPos = expr.indexOf('(');
        String functionName = expr.substring(0, parenPos);
        String argument = expr.substring(parenPos + 1, expr.length() - 1);

        // List of supported mathematical functions
        String[] supportedFunctions = {"sqrt", "cbrt", "abs", "sin", "cos", "tan", "ln", "log", "exp"};

        boolean isValidFunction = false;
        for (String func : supportedFunctions) {
            if (functionName.equals(func)) {
                isValidFunction = true;
                break;
            }
        }

        if (!isValidFunction) {
            return false;
        }

        // Recursively validate the argument
        return isValidMathExpression(argument);
    }
}