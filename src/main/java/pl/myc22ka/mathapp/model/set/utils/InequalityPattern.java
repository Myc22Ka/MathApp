package pl.myc22ka.mathapp.model.set.utils;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.sets.BoundType;
import pl.myc22ka.mathapp.model.set.sets.Interval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.myc22ka.mathapp.model.set.SetSymbols.INFINITY;
import static pl.myc22ka.mathapp.model.set.sets.BoundType.CLOSED;
import static pl.myc22ka.mathapp.model.set.sets.BoundType.OPEN;

public enum InequalityPattern {
    GREATER_EQUAL("x\\s*>=\\s*([+-]?\\d*\\.?\\d+)", true, CLOSED, OPEN),
    GREATER("x\\s*>\\s*([+-]?\\d*\\.?\\d+)", true, OPEN, OPEN),
    LESS_EQUAL("x\\s*<=\\s*([+-]?\\d*\\.?\\d+)", false, OPEN, CLOSED),
    LESS("x\\s*<\\s*([+-]?\\d*\\.?\\d+)", false, OPEN, OPEN),
    GREATER_EQUAL_UNICODE("x\\s*≥\\s*([+-]?\\d*\\.?\\d+)", true, CLOSED, OPEN),
    LESS_EQUAL_UNICODE("x\\s*≤\\s*([+-]?\\d*\\.?\\d+)", false, OPEN, CLOSED);

    private final Pattern pattern;
    private final boolean isLowerBound; // true: [value, ∞), false: (-∞, value]
    private final BoundType leftBound;
    private final BoundType rightBound;

    InequalityPattern(String regex, boolean isLowerBound, BoundType leftBound, BoundType rightBound) {
        this.pattern = Pattern.compile("^" + regex + "$");
        this.isLowerBound = isLowerBound;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    public Matcher matcher(String input) {
        return pattern.matcher(input);
    }

    public boolean matches(String input) {
        return matcher(input).matches();
    }

    public ISet toInterval(String value, ExprEvaluator evaluator) {
        if (isLowerBound) {
            IExpr start = evaluator.eval(value);
            return new Interval(start, leftBound, rightBound, INFINITY.parse());
        } else {
            return new Interval(INFINITY.parse().negate(), leftBound, rightBound, evaluator.eval(value));
        }
    }
}
