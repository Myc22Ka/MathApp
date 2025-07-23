package pl.myc22ka.mathapp.model.set.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.SetSymbols;

import java.util.List;

/**
 * Utility class for operations on string expressions,
 * especially for analyzing and manipulating operators and parentheses at the top level.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
@UtilityClass
public class ExpressionUtils {

    /**
     * Checks if the expression contains more than one top-level operator.
     *
     * @param expr the expression string to analyze
     * @return true if there are multiple top-level operators; false otherwise
     */
    public boolean containsMultipleOperators(@NotNull String expr) {
        return countTopLevelOperators(expr, SetSymbols.getBinaryOperations()) > 1;
    }

    /**
     * Checks if the expression contains at least one top-level operator.
     *
     * @param expr the expression string to analyze
     * @return true if there is at least one top-level operator; false otherwise
     */
    public boolean containsBinaryOperators(@NotNull String expr) {
        return countTopLevelOperators(expr, SetSymbols.getBinaryOperations()) > 0;
    }

    /**
     * Counts how many times operators from the given list appear at the top level
     * of the expression (i.e., outside of any parentheses, brackets, or braces).
     *
     * @param expr the expression string to analyze
     * @param ops  the list of operators to count
     * @return the number of top-level operators found
     */
    public int countTopLevelOperators(@NotNull String expr, @NotNull List<SetSymbols> ops) {
        int count = 0;
        Depth depth = new Depth();
        for (int i = 0; i < expr.length(); i++) {
            depth.update(expr.charAt(i));
            if (depth.isTop()) {
                for (SetSymbols op : ops) {
                    if (expr.startsWith(op.toString(), i)) {
                        count++;
                        i += op.toString().length() - 1;
                        break;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Finds the position of the first comma at the top level
     * (not enclosed inside parentheses) in the given content string.
     *
     * @param content the string to search
     * @return the index of the first top-level comma, or -1 if none found
     */
    public int findTopLevelComma(@NotNull String content) {
        int depth = 0;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (c == ',' && depth == 0) return i;
        }
        return -1;
    }

    /**
     * Finds the position of the given operator string in the expression,
     * only if it appears at the top level (outside of parentheses, brackets, etc.).
     *
     * @param expr the expression string to search
     * @param op   the operator string to find
     * @return the index of the operator if found at top level; -1 otherwise
     */
    public int findOperatorPosition(@NotNull String expr, @NotNull String op) {
        Depth depth = new Depth();
        for (int i = 0; i <= expr.length() - op.length(); i++) {
            depth.update(expr.charAt(i));
            if (depth.isTop() && expr.startsWith(op, i)) return i;
        }
        return -1;
    }

    /**
     * Removes the outermost pair of parentheses from the expression string.
     *
     * @param expr the expression string to strip
     * @return the expression without outermost parentheses, or original if none to strip
     */
    public @NotNull String stripOuterParentheses(@NotNull String expr) {
        if (!expr.startsWith("(") || !expr.endsWith(")")) return expr;
        if (hasMatchingOuterParentheses(expr)) {
            return expr.substring(1, expr.length() - 1);
        }
        return expr;
    }

    /**
     * Checks if the outermost parentheses in the expression match properly,
     * and the expression is fully enclosed by them (no unmatched closing parenthesis before the end).
     *
     * @param expr the expression string to check
     * @return true if the outer parentheses match and encompass the whole expression; false otherwise
     */
    private boolean hasMatchingOuterParentheses(@NotNull String expr) {
        int depth = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            if (depth == 0 && i < expr.length() - 1) return false;
        }
        return depth == 0;
    }
}
