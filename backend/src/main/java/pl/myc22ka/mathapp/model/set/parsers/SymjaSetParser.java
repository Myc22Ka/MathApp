package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.ExprEvaluator;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.utils.ExpressionUtils;
import pl.myc22ka.mathapp.model.set.utils.InequalityPattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.myc22ka.mathapp.model.set.SetSymbols.*;

/**
 * Parser for Multiple set exprs, such as "x<-3∨(-3<x∧x<1)".
 * It produces a {@link Fundamental} or {@link Interval} set representation.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
public final class SymjaSetParser implements ISetParser{

    private final ExprEvaluator evaluator = new ExprEvaluator();

    private static final Pattern AND_PATTERN = Pattern.compile("(.+)\\s*∧\\s*(.+)");
    private static final Pattern OR_PATTERN = Pattern.compile("(.+)\\s*∨\\s*(.+)");

    @Override
    public boolean canHandle(@NotNull String expr) {
        if (expr.equals("True") || expr.equals("False")) return true;

        if (AND_PATTERN.matcher(expr).matches() || OR_PATTERN.matcher(expr).matches()) {
            return true;
        }

        for (InequalityPattern pattern : InequalityPattern.values()) {
            if (pattern.matches(expr)) return true;
        }
        return false;
    }

    @Override
    public @NotNull ISet parse(@NotNull String expr) {
        if (expr.equals("True")) {
            return new Fundamental(REAL);
        }

        if (expr.equals("False")) {
            return new Fundamental(EMPTY);
        }

        expr = ExpressionUtils.stripOuterParentheses(expr);

        var logicOperations = getSetLogicSymbols();

        for (var operation : logicOperations) {
            int position = ExpressionUtils.findOperatorPosition(expr, operation.toString());
            if (position != -1) {
                String leftExpr = expr.substring(0, position);
                String rightExpr = expr.substring(position + 1);

                ISet leftSet = parse(leftExpr);
                ISet rightSet = parse(rightExpr);

                return operation.equals(OR) ? leftSet.union(rightSet) : leftSet.intersection(rightSet);
            }
        }

        for (InequalityPattern pattern : InequalityPattern.values()) {
            Matcher matcher = pattern.matcher(expr);
            if (matcher.matches()) {
                String value = matcher.group(1);
                return pattern.toInterval(value, evaluator);
            }
        }

        throw new IllegalArgumentException("Unsupported inequality expr: " + expr);
    }
}
