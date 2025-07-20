package pl.myc22ka.mathapp.model.set.sets;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.ISetType;
import pl.myc22ka.mathapp.model.set.Set;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.visitors.SetVisitor;
import pl.myc22ka.mathapp.model.set.visitors.DifferenceVisitor;
import pl.myc22ka.mathapp.model.set.visitors.IntersectionVisitor;
import pl.myc22ka.mathapp.model.set.visitors.UnionVisitor;

import java.util.ArrayList;
import java.util.List;

import static pl.myc22ka.mathapp.model.set.SetSymbols.DIFFERENCE;
import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

/**
 * Mathematical interval set [0, 4].
 *
 * @author Myc22Ka
 * @version 1.0
 * @since 2025‑06‑19
 */
public class Interval implements ISet {
    private final ExprEvaluator evaluator = new ExprEvaluator();
    private IExpr expression;

    public Interval(IExpr start, @NotNull BoundType left, @NotNull BoundType right, IExpr end) {
        this.expression = evaluator.eval("IntervalData({" + start + "," + left.getInclusive() + "," + right.getInclusive() + "," + end + "})");
    }

    public Interval(String expression) {
        try {
            this.expression = evaluator.eval(expression);
        } catch (Exception e) {
            this.expression = fromStringToInterval(expression);
        }
    }

    private @NotNull IExpr fromStringToInterval(@NotNull String expression) {
        String trimmed = expression.trim();

        // Basic structure validation
        if (!trimmed.matches("^\\s*[\\[(].*[])]\\s*$")) {
            throw new IllegalArgumentException("Invalid interval format: " + expression);
        }

        // Extract bracket types
        char leftBracket = trimmed.charAt(trimmed.indexOf('[') != -1 ? trimmed.indexOf('[') : trimmed.indexOf('('));
        char rightBracket = trimmed.charAt(trimmed.lastIndexOf(']') != -1 ? trimmed.lastIndexOf(']') : trimmed.lastIndexOf(')'));

        BoundType leftBound = (leftBracket == '[') ? BoundType.CLOSED : BoundType.OPEN;
        BoundType rightBound = (rightBracket == ']') ? BoundType.CLOSED : BoundType.OPEN;

        // Extract content between brackets
        String content = trimmed.substring(1, trimmed.length() - 1).trim();

        // Find the comma that separates bounds (accounting for nested parentheses)
        int commaPos = findTopLevelComma(content);
        if (commaPos == -1) {
            throw new IllegalArgumentException("Invalid interval format: " + expression);
        }

        String startStr = content.substring(0, commaPos).trim();
        String endStr = content.substring(commaPos + 1).trim();

        try {
            IExpr start = evaluator.parse(startStr);
            IExpr end = evaluator.parse(endStr);
            return new Interval(start, leftBound, rightBound, end).getExpression();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid interval format: " + expression + " - " + e.getMessage());
        }
    }

    /**
     * Finds the position of a comma at the top level (not inside parentheses)
     * This handles mathematical functions like sqrt(10) properly
     */
    private static int findTopLevelComma(@NotNull String content) {
        int parenDepth = 0;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '(') {
                parenDepth++;
            } else if (c == ')') {
                parenDepth--;
            } else if (c == ',' && parenDepth == 0) {
                return i;
            }
        }

        return -1; // No top-level comma found
    }

    @Override
    public <T> T accept(@NotNull SetVisitor<T> visitor) {
        return visitor.visitInterval(this);
    }

    @Override
    public Interval toInterval() {
        return this;
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

                if(start.equals(F.CNInfinity.toString())){
                    start = SetSymbols.NEGATIVE_INFINITY.toString();
                }

                if(end.equals(F.Infinity.toString())){
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