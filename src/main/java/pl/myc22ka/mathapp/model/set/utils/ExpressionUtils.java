package pl.myc22ka.mathapp.model.set.utils;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.SetSymbols;

import java.util.List;

public class ExpressionUtils {
    public static boolean containsMultipleOperators(@NotNull String expr) {
        return countTopLevelOperators(expr, SetSymbols.getBinaryOperations()) > 1;
    }

    public static boolean containsBinaryOperators(@NotNull String expr) {
        return countTopLevelOperators(expr, SetSymbols.getBinaryOperations()) > 0;
    }

    public static int countTopLevelOperators(@NotNull String expr, @NotNull List<SetSymbols> ops) {
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

    public static int findTopLevelComma(@NotNull String content) {
        int depth = 0;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (c == ',' && depth == 0) return i;
        }
        return -1;
    }

    public static int findOperatorPosition(@NotNull String expr, @NotNull String op) {
        Depth depth = new Depth();
        for (int i = 0; i <= expr.length() - op.length(); i++) {
            depth.update(expr.charAt(i));
            if (depth.isTop() && expr.startsWith(op, i)) return i;
        }
        return -1;
    }

    public static @NotNull String stripOuterParentheses(@NotNull String expr) {
        if (!expr.startsWith("(") || !expr.endsWith(")")) return expr;
        if (hasMatchingOuterParentheses(expr)) {
            return expr.substring(1, expr.length() - 1);
        }
        return expr;
    }

    private static boolean hasMatchingOuterParentheses(@NotNull String expr) {
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
