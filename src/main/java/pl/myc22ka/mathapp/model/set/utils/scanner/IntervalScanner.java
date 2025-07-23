package pl.myc22ka.mathapp.model.set.utils.scanner;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.SetSymbols;

/**
 * Utility class for identifying and working with intervals in set expressions.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
@UtilityClass
public class IntervalScanner {

    /**
     * Marks which characters in the input string belong to a valid interval (e.g. [1, 2)).
     *
     * @param expression the full set expression
     * @return a boolean array where true indicates the character is part of an interval
     */
    public boolean[] markIntervalCharacters(@NotNull String expression) {
        boolean[] isInterval = new boolean[expression.length()];

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '[' || c == '(') {
                IntervalInfo info = analyzeInterval(expression, i);
                if (info.isInterval()) {
                    for (int j = info.start(); j < info.end(); j++) {
                        isInterval[j] = true;
                    }
                }
            }
        }

        return isInterval;
    }

    /**
     * Finds the beginning index of an interval starting at or before the given position.
     *
     * @param pos the position to search backwards from
     * @param isInterval the boolean array marking interval characters
     * @return the index of the interval start
     */
    public int findIntervalStart(int pos, boolean[] isInterval) {
        int i = pos;
        while (i >= 0 && isInterval[i]) i--;
        return i + 1;
    }

    /**
     * Finds the ending index of an interval starting at a given position.
     *
     * @param expression the full expression
     * @param start the starting index of the interval
     * @param isInterval the boolean array marking interval characters
     * @return the index just after the interval ends
     */
    public int findIntervalEnd(@NotNull String expression, int start, boolean[] isInterval) {
        int i = start;
        while (i < expression.length() && isInterval[i]) i++;
        return i;
    }

    /**
     * Finds the ending index of a complete operand (finite set, interval, or fundamental symbol).
     *
     * @param expression the full expression
     * @param start the starting index of the operand
     * @param isInterval the boolean array marking interval characters
     * @return the index just after the operand ends
     */
    public int findOperandEnd(@NotNull String expression, int start, boolean[] isInterval) {
        int i = start;
        char startChar = expression.charAt(start);

        if (startChar == '{') {
            int braceCount = 1;
            i++;
            while (i < expression.length() && braceCount > 0) {
                if (expression.charAt(i) == '{') braceCount++;
                else if (expression.charAt(i) == '}') braceCount--;
                i++;
            }
            return i;
        } else if ((startChar == '[' || startChar == '(') && isInterval[start]) {
            return findIntervalEnd(expression, start, isInterval);
        } else {
            while (i < expression.length()) {
                char c = expression.charAt(i);
                if (SetSymbols.getBinaryOperationsString().indexOf(c) != -1 ||
                        ((c == '(' || c == ')') && !isInterval[i])) break;
                i++;
            }
            return i;
        }
    }

    private @NotNull IntervalInfo analyzeInterval(@NotNull String expression, int pos) {
        char startChar = expression.charAt(pos);
        if (startChar != '[' && startChar != '(') return new IntervalInfo(false, pos, pos);

        int i = pos + 1;
        int depth = 1;
        int commaCount = 0;
        boolean hasSetOps = false;

        while (i < expression.length() && depth > 0) {
            char c = expression.charAt(i);
            if (c == '[' || c == '(') depth++;
            else if (c == ']' || c == ')') {
                depth--;
                if (depth == 0) {
                    if (commaCount == 1 && !hasSetOps) return new IntervalInfo(true, pos, i + 1);
                    else return new IntervalInfo(false, pos, pos);
                }
            } else if (depth == 1) {
                if (c == ',') commaCount++;
                else if (SetSymbols.getBinaryOperationsString().indexOf(c) != -1) hasSetOps = true;
            }
            i++;
        }
        return new IntervalInfo(false, pos, pos);
    }
}
