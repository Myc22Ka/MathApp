package pl.myc22ka.mathapp.model.set.sets;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.ISetType;
import pl.myc22ka.mathapp.model.set.Set;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.visitors.DifferenceVisitor;
import pl.myc22ka.mathapp.model.set.visitors.IntersectionVisitor;
import pl.myc22ka.mathapp.model.set.visitors.SetVisitor;
import pl.myc22ka.mathapp.model.set.visitors.UnionVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Mathematical interval set [0, 4].
 *
 * @author Myc22Ka
 * @version 1.0.3
 * @since 2025.06.19
 */
public class Interval implements ISet {
    private final ExprEvaluator evaluator = new ExprEvaluator();
    private IExpr expression;

    /**
     * Creates an Interval with the given start and end points and specified bound types.
     *
     * @param start the lower bound of the interval
     * @param left  the bound type for the left side (OPEN or CLOSED)
     * @param right the bound type for the right side (OPEN or CLOSED)
     * @param end   the upper bound of the interval
     */
    public Interval(IExpr start, @NotNull BoundType left, @NotNull BoundType right, IExpr end) {
        this.expression = evaluator.eval("IntervalData({" + start + "," + left.getInclusive() + "," + right.getInclusive() + "," + end + "})");
    }

    /**
     * Creates an interval by evaluating a Symja expression string.
     *
     * @param expression the Symja expression that evaluates to an IntervalData object
     */
    public Interval(String expression) {
        this.expression = evaluator.eval(expression);
    }

    @Override
    public <T> T accept(@NotNull SetVisitor<T> visitor) {
        return visitor.visitInterval(this);
    }

    @Override
    public Interval toInterval() {
        return this;
    }

    public ISet findAllIntegers() {
        if (expression == null || expression.toString().equals("IntervalData()")) {
            return new Fundamental(SetSymbols.EMPTY);
        }

        List<IExpr> integers = new ArrayList<>();

        if (expression.getAt(1).isList()) {
            for (int i = 1; i < expression.size(); i++) {
                IExpr element = expression.getAt(i);

                int first = (int) Math.ceil(element.getAt(1).evalf());
                int last = (int) Math.floor(element.getAt(4).evalf());

                Interval interval = new Interval(
                        element.getAt(1),
                        BoundType.fromInclusive(element.getAt(2).toString()),
                        BoundType.fromInclusive(element.getAt(3).toString()),
                        element.getAt(4)
                );

                for (int k = first; k <= last; k++) {
                    IExpr candidate = evaluator.eval(Integer.toString(k));

                    String boundCheck = "IntervalData({" + candidate + "," + BoundType.CLOSED.getInclusive() + "," + BoundType.CLOSED.getInclusive() + "," + candidate + "})";

                    if (interval.contains(boundCheck)) {
                        integers.add(candidate);
                    }
                }
            }
        }

        if (integers.isEmpty()) {
            return new Fundamental(SetSymbols.EMPTY);
        }
        return new Finite(integers);
    }

    /**
     * Attempts to simplify the current set representation.
     *
     * <p>If the {@link ReducedFundamental} form of this set is shorter as a string
     * and represents a {@link Finite} set, it returns that reduced form.
     * Otherwise, it returns this set unchanged.</p>
     *
     * @return a simplified version of this set if possible; otherwise, the original set
     */
    public ISet shorten() {
        var rFundamental = this.toReducedFundamental();

        if (this.toString().length() > rFundamental.toString().length() && Set.of(rFundamental.getRight().toString()).getISetType() == ISetType.FINITE) {
            return rFundamental;
        }

        return this;
    }

    /**
     * Converts this set into a {@link ReducedFundamental} form by expressing it
     * as the difference between the universal set {@link SetSymbols#REAL} and its complement.
     *
     * @return a {@link ReducedFundamental} representing this set in simplified form
     */
    public ReducedFundamental toReducedFundamental() {
        var universe = new Fundamental(SetSymbols.REAL);

        return new ReducedFundamental(universe, SetSymbols.DIFFERENCE, this.complement(universe));
    }

    @Override
    public @NotNull IExpr getExpression() {
        return expression;
    }

    @Override
    public @NotNull ISetType getISetType() {
        return ISetType.INTERVAL;
    }

    @Override
    public boolean contains(@NotNull String element) {
        return evaluator.eval("IntervalMemberQ[" + expression + "," + element + "]").isTrue();
    }

    @Override
    public void remove(@NotNull String element) {
        expression = difference(Set.of(element)).getExpression();
    }

    @Override
    public Integer size() {
        return null;
    }

    @Override
    public @NotNull ISet union(@NotNull ISet other) {
        return other.accept(new UnionVisitor(this));
    }

    @Override
    public @NotNull ISet difference(@NotNull ISet other) {
        return other.accept(new DifferenceVisitor(this));
    }

    @Override
    public @NotNull ISet intersection(@NotNull ISet other) {
        return other.accept(new IntersectionVisitor(this));
    }

    @Override
    public String toString() {
        if (expression == null || expression.toString().equals("IntervalData()")) return SetSymbols.EMPTY.toString();

        List<IExpr> toFiniteList = new ArrayList<>();
        List<String> intervalList = new ArrayList<>();

        if (expression.getAt(1).isList()) {
            for (int i = 1; i < expression.size(); i++) {
                IExpr element = expression.getAt(i);

                String start = element.getAt(1).toString();
                String leftSym = element.getAt(2).toString();
                String rightSym = element.getAt(3).toString();
                String end = element.getAt(4).toString();

                if (start.equals(F.CNInfinity.toString())) {
                    start = SetSymbols.NEGATIVE_INFINITY.toString();
                }

                if (end.equals(F.Infinity.toString())) {
                    end = SetSymbols.INFINITY.toString();
                }

                boolean endpointsEqual = start.equals(end);

                boolean bothClosed = leftSym.equals(BoundType.CLOSED.toString())
                        && rightSym.equals(BoundType.CLOSED.toString());

                if (endpointsEqual && bothClosed) {
                    toFiniteList.add(element.getAt(1));
                    continue;
                }

                BoundType leftBound = BoundType.fromInclusive(leftSym);
                BoundType rightBound = BoundType.fromInclusive(rightSym);

                String leftBracket = leftBound.toBracket(true);
                String rightBracket = rightBound.toBracket(false);

                intervalList.add(leftBracket + start + "," + end + rightBracket);

            }
        }

        String finitePart = "";
        if (!toFiniteList.isEmpty()) {
            Finite finite = new Finite(toFiniteList);
            finitePart = finite.getExpression().toString();
        }

        String intervalPart = String.join(SetSymbols.UNION.toString(), intervalList);

        if (!finitePart.isEmpty() && !intervalPart.isEmpty()) {
            return intervalPart + SetSymbols.UNION + finitePart;
        } else if (!intervalPart.isEmpty()) {
            return intervalPart;
        } else {
            return finitePart;
        }
    }
}