package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.BoundType;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.utils.ExpressionUtils;

import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;
import static pl.myc22ka.mathapp.model.set.sets.BoundType.CLOSED;
import static pl.myc22ka.mathapp.model.set.sets.BoundType.OPEN;

public class IntervalParser implements ISetParser{

    @Override
    public boolean canHandle(@NotNull String expr) {
        return expr.matches("^\\s*[\\[(].*[])]\\s*$");
    }

    @Override
    public @NotNull ISet parse(@NotNull String expr) {

        if(SetSymbols.isReal(expr)) return new Fundamental(REAL);

        String trimmed = expr.trim();
        char left = trimmed.charAt(0);
        char right = trimmed.charAt(trimmed.length() - 1);
        BoundType leftBound = (left == '[') ? CLOSED : OPEN;
        BoundType rightBound = (right == ']') ? CLOSED : OPEN;

        String content = trimmed.substring(1, trimmed.length() - 1);
        int comma = ExpressionUtils.findTopLevelComma(content);

        String start = content.substring(0, comma).trim();
        String end = content.substring(comma + 1).trim();

        String symjaExpr = String.format("IntervalData({%s, %s, %s, %s})", start, leftBound, rightBound, end);
        return new Interval(symjaExpr);
    }
}