package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.ExprEvaluator;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.BoundType;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.utils.ExpressionUtils;

import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;
import static pl.myc22ka.mathapp.model.set.sets.BoundType.CLOSED;
import static pl.myc22ka.mathapp.model.set.sets.BoundType.OPEN;

/**
 * Parser for Fundamental set expressions, such as "(0,1), [0, ∞)".
 * It produces a {@link Interval} set representation.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 22.07.2025
 */
public final class IntervalParser implements ISetParser{

    @Override
    public boolean canHandle(@NotNull String expr) {
        return expr.matches("^\\s*[\\[(].*[])]\\s*$");
    }

    @Override
    public @NotNull ISet parse(@NotNull String expr) {

        if(SetSymbols.isReal(expr)) return new Fundamental(REAL);

        char left = expr.charAt(0);
        char right = expr.charAt(expr.length() - 1);
        BoundType leftBound = (left == '[') ? CLOSED : OPEN;
        BoundType rightBound = (right == ']') ? CLOSED : OPEN;

        String content = expr.substring(1, expr.length() - 1);
        int comma = ExpressionUtils.findTopLevelComma(content);

        String start = content.substring(0, comma).trim();
        String end = content.substring(comma + 1).trim();

        ExprEvaluator evaluator = new ExprEvaluator();

        return new Interval(evaluator.eval(start), leftBound, rightBound, evaluator.eval(end));
    }
}