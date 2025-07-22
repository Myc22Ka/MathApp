package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.SetSymbols;

public class IntervalScanner {
    private static class IntervalInfo {
        boolean isInterval;
        int start;
        int end;

        IntervalInfo(boolean isInterval, int start, int end) {
            this.isInterval = isInterval;
            this.start = start;
            this.end = end;
        }
    }

    public static boolean[] markIntervalCharacters(@NotNull String expression) {
        boolean[] isInterval = new boolean[expression.length()];

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '[' || c == '(') {
                IntervalInfo info = analyzeInterval(expression, i);
                if (info.isInterval) {
                    for (int j = info.start; j < info.end; j++) {
                        isInterval[j] = true;
                    }
                }
            }
        }

        return isInterval;
    }

    public static int findIntervalStart(@NotNull String expression, int pos, boolean[] isInterval) {
        int i = pos;
        while (i >= 0 && isInterval[i]) i--;
        return i + 1;
    }

    public static int findIntervalEnd(@NotNull String expression, int start, boolean[] isInterval) {
        int i = start;
        while (i < expression.length() && isInterval[i]) i++;
        return i;
    }

    public static int findOperandEnd(@NotNull String expression, int start, boolean[] isInterval) {
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

    private static IntervalInfo analyzeInterval(@NotNull String expression, int pos) {
        char startChar = expression.charAt(pos);
        if (startChar != '[' && startChar != '(') return new IntervalInfo(false, pos, pos);

        int i = pos + 1;
        int depth = 1;
        int commaCount = 0;
        boolean hasSetOps = false;
        char expectedClose = (startChar == '[') ? ']' : ')';

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
