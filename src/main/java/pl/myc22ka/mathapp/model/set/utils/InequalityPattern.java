package pl.myc22ka.mathapp.model.set.utils;

import org.jetbrains.annotations.NotNull;
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

/**
 * Enum representing different forms of inequalities involving a single variable (e.g., x > 3, 5 ≤ x).
 * <p>
 * Each enum constant corresponds to a specific pattern for matching and interpreting a one-sided inequality,
 * and provides the information needed to convert the inequality or Symja set expressions into an {@link ISet} object.
 * <p>
 * It supports both left-sided (e.g., {@code x > 3}) and reversed right-sided forms (e.g., {@code 3 < x}),
 * including both ASCII and Unicode inequality symbols.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
public enum InequalityPattern {
    /**
     * Matches expressions like "x >= 3"
     */
    GREATER_EQUAL("[a-zA-Z]\\s*>=\\s*([+-]?\\d*\\.?\\d+)", true, CLOSED, OPEN),

    /**
     * Matches expressions like "x > 3"
     */
    GREATER("[a-zA-Z]\\s*>\\s*([+-]?\\d*\\.?\\d+)", true, OPEN, OPEN),

    /**
     * Matches expressions like "x <= 3"
     */
    LESS_EQUAL("[a-zA-Z]\\s*<=\\s*([+-]?\\d*\\.?\\d+)", OPEN, CLOSED),

    /**
     * Matches expressions like "x < 3"
     */
    LESS("[a-zA-Z]\\s*<\\s*([+-]?\\d*\\.?\\d+)", OPEN, OPEN),

    /**
     * Matches expressions like "x ≥ 3" (Unicode greater or equal)
     */
    GREATER_EQUAL_UNICODE("[a-zA-Z]\\s*≥\\s*([+-]?\\d*\\.?\\d+)", true, CLOSED, OPEN),

    /**
     * Matches expressions like "x ≤ 3" (Unicode less or equal)
     */
    LESS_EQUAL_UNICODE("[a-zA-Z]\\s*≤\\s*([+-]?\\d*\\.?\\d+)", OPEN, CLOSED),

    /**
     * Matches expressions like "-3 > x" (equivalent to x < -3)
     */
    REVERSED_GREATER("([+-]?\\d*\\.?\\d+)\\s*>\\s*[a-zA-Z]", OPEN, OPEN),

    /**
     * Matches expressions like "5 >= x" (equivalent to x <= 5)
     */
    REVERSED_GREATER_EQUAL("([+-]?\\d*\\.?\\d+)\\s*>=\\s*[a-zA-Z]", OPEN, CLOSED),

    /**
     * Matches expressions like "-2 < x" (equivalent to x > -2)
     */
    REVERSED_LESS("([+-]?\\d*\\.?\\d+)\\s*<\\s*[a-zA-Z]", true, OPEN, OPEN),

    /**
     * Matches expressions like "10 <= x" (equivalent to x >= 10)
     */
    REVERSED_LESS_EQUAL("([+-]?\\d*\\.?\\d+)\\s*<=\\s*[a-zA-Z]", true, CLOSED, OPEN),

    /**
     * Matches expressions like "5 ≥ x" (equivalent to x ≤ 5, Unicode)
     */
    REVERSED_GREATER_EQUAL_UNICODE("([+-]?\\d*\\.?\\d+)\\s*≥\\s*[a-zA-Z]", OPEN, CLOSED),

    /**
     * Matches expressions like "10 ≤ x" (equivalent to x ≥ 10, Unicode)
     */
    REVERSED_LESS_EQUAL_UNICODE("([+-]?\\d*\\.?\\d+)\\s*≤\\s*[a-zA-Z]", true, CLOSED, OPEN);

    private final Pattern pattern;
    private final boolean isLowerBound; // true: [value, ∞), false: (-∞, value]
    private final BoundType leftBound;
    private final BoundType rightBound;

    /**
     * Constructs an inequality pattern with associated properties.
     *
     * @param regex        The regular expression for matching the inequality.
     * @param isLowerBound Whether the pattern represents a lower bound (true) or upper bound (false).
     * @param leftBound    The bound type on the left side of the interval.
     * @param rightBound   The bound type on the right side of the interval.
     */
    InequalityPattern(String regex, boolean isLowerBound, BoundType leftBound, BoundType rightBound) {
        this.pattern = Pattern.compile("^" + regex + "$");
        this.isLowerBound = isLowerBound;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    /**
     * Constructs an inequality pattern without a lower bound (i.e. upper bound).
     *
     * @param regex      The regular expression for matching the inequality.
     * @param leftBound  The bound type on the left side of the resulting interval.
     * @param rightBound The bound type on the right side of the resulting interval.
     */
    InequalityPattern(String regex, BoundType leftBound, BoundType rightBound) {
        this(regex, false, leftBound, rightBound);
    }

    /**
     * Creates a matcher for the given input using the pattern.
     *
     * @param input The input string to match against.
     * @return A Matcher object.
     */
    public @NotNull Matcher matcher(String input) {
        return pattern.matcher(input);
    }

    /**
     * Checks if the given input fully matches this inequality pattern.
     *
     * @param input The input string to match.
     * @return True if the input matches; false otherwise.
     */
    public boolean matches(String input) {
        return matcher(input).matches();
    }

    /**
     * Converts the matched inequality value into an Interval representation.
     *
     * @param value     The numeric part of the inequality (as string).
     * @param evaluator The Symja evaluator used to parse the value.
     * @return An Interval object representing the inequality.
     */
    public @NotNull ISet toInterval(String value, ExprEvaluator evaluator) {
        if (isLowerBound) {
            IExpr start = evaluator.eval(value);
            return new Interval(start, leftBound, rightBound, INFINITY.parse());
        } else {
            return new Interval(INFINITY.parse().negate(), leftBound, rightBound, evaluator.eval(value));
        }
    }
}
