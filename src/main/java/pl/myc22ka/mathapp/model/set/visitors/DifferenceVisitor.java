package pl.myc22ka.mathapp.model.set.visitors;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.Finite;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.sets.ReducedFundamental;

import java.util.ArrayList;
import java.util.List;

import static pl.myc22ka.mathapp.model.set.ISetType.*;

/**
 * Visitor for computing the set difference (A ∖ B).
 *
 * @author Myc22Ka
 * @version 1.0
 * @since 2025-06-19
 */
public class DifferenceVisitor implements SetVisitor<ISet> {
    private final ISet left;
    private final ExprEvaluator evaluator = new ExprEvaluator();

    public DifferenceVisitor(ISet left) {
        this.left = left;
    }

    @Override
    public ISet visitFinite(Finite right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        // (A ∪ B = B ∪ A)
        if (left.getISetType() == INTERVAL) {
            return visitInterval(right.toInterval());
        }

        // Check if REDUCED_FUNDAMENTAL could be simplified
        if (left instanceof ReducedFundamental rLeft) {
            var simplified = rLeft.simplify();

            if (!(simplified instanceof ReducedFundamental))
                return simplified.difference(right);
        }

        // Case for FUNDAMENTAL and REDUCED_FUNDAMENTAL
        if (left.getISetType() == FUNDAMENTAL || left.getISetType() == REDUCED_FUNDAMENTAL) {

            if (left instanceof ReducedFundamental rLeft) {
                ISet newRight = rLeft.getRight().union(right);

                if (newRight.isEmpty()) {
                    return new Fundamental(rLeft.getLeftSymbol());
                }

                return new ReducedFundamental(
                        new Fundamental(rLeft.getLeftSymbol()),
                        SetSymbols.DIFFERENCE,
                        newRight,
                        false
                );
            }

            List<IExpr> includedElements = SetUtils.getIncludedElements(left, right);

            if (includedElements.isEmpty()) return left;

            return new ReducedFundamental(left, SetSymbols.DIFFERENCE, new Finite(includedElements), false);
        }

        IExpr expr = evaluator.eval(F.Complement(left.getExpression(), right.getExpression()));

        if (expr.isList()) return new Finite(expr);

        return new Fundamental(SetSymbols.EMPTY);
    }

    @Override
    public ISet visitInterval(Interval right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        ISet normalizedLeft = (left instanceof Finite) ? left.toInterval() : left;

        if (left instanceof Fundamental fLeft) {
            if (!fLeft.getLeftSymbol().equals(SetSymbols.REAL)) {
                return new ReducedFundamental(left, SetSymbols.DIFFERENCE, right, false);
            }

            normalizedLeft = left.toInterval();
        }

        // (U ∖ A)∖ B = (A ∪ B)'
        if (left instanceof ReducedFundamental rLeft) {
            var union = rLeft.getRight().union(right);

            // symbolic solution
            if (rLeft.getLeft().getISetType() == FUNDAMENTAL && rLeft.getRight().getISetType() == FUNDAMENTAL) {
                return new ReducedFundamental(
                        new ReducedFundamental(rLeft.getLeft(), rLeft.getOperation(), rLeft.getRight(), true),
                        SetSymbols.DIFFERENCE,
                        right,
                        false
                );
            }

            return union.complement(rLeft.getLeft());
        }

        IExpr expr = evaluator.eval(
                F.IntervalComplement(normalizedLeft.getExpression(), right.getExpression())
        );

        return new Interval(expr.toString());
    }

    @Override
    public ISet visitFundamental(Fundamental right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        if (left.toString().equals(right.toString())) return new Fundamental(SetSymbols.EMPTY);

        if (left instanceof Finite fLeft) {
            List<IExpr> excludedElements = SetUtils.getExcludedElements(right, fLeft);
            return excludedElements.isEmpty() ? new Fundamental(SetSymbols.EMPTY) : new Finite(excludedElements);
        }

        if (left instanceof Fundamental fLeft) {
            if (fLeft.getLeftSymbol().isSubsetOf(right.getLeftSymbol())) {
                return new Fundamental(SetSymbols.EMPTY);
            }
            return new ReducedFundamental(left, SetSymbols.DIFFERENCE, right, false);
        }

        if (left.getISetType() == INTERVAL) {

            if(right.getLeftSymbol().equals(SetSymbols.REAL)){
                return new Fundamental(SetSymbols.EMPTY);
            }

            return new ReducedFundamental(left, SetSymbols.DIFFERENCE, right, false);
        }

        if (left.getISetType() == REDUCED_FUNDAMENTAL) {
            ReducedFundamental rLeft = (ReducedFundamental) left;

            if (rLeft.getOperation() == SetSymbols.DIFFERENCE) {

                if (rLeft.getLeftSymbol().equals(right.getLeftSymbol())) {
                    return new Fundamental(SetSymbols.EMPTY);
                }

                var newRight = rLeft.getRight().union(right);

                return new ReducedFundamental(rLeft.getLeft(), SetSymbols.DIFFERENCE, newRight, false);
            }
        }

        return new Fundamental(SetSymbols.EMPTY);
    }

    @Override
    public ISet visitReducedFundamental(ReducedFundamental right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        if (left.toString().equals(right.getLeftSymbol().toString())) return right.getRight();

        if (left instanceof Finite fLeft) {
            List<IExpr> excludedElements = SetUtils.getExcludedElements(right, fLeft);
            return excludedElements.isEmpty() ? new Fundamental(SetSymbols.EMPTY) : new Finite(excludedElements);
        }

        if (left.getISetType() == INTERVAL) {
            Interval iLeft = (Interval) left;

            if (right.getLeft() instanceof Fundamental fRight
                    && SetSymbols.isReal(fRight.getLeftSymbol().toString())
                    && right.getOperation() == SetSymbols.DIFFERENCE) {

                ISet excludedSet = right.getRight();

                ISet intersection = iLeft.intersection(excludedSet);

                return intersection.isEmpty() ? new Fundamental(SetSymbols.EMPTY) : intersection;
            }

            if(right.getLeft() instanceof Fundamental fRight && SetSymbols.isReal(fRight.getLeftSymbol().toString())) {
                return new ReducedFundamental(
                        left,
                        SetSymbols.DIFFERENCE,
                        new ReducedFundamental(right.getLeft(), right.getOperation(), right.getRight(), true),
                        false
                );
            }

            return iLeft.difference(right);
        }

        // N - (R - Z) = (N ∩ Z) ∪ (N - R)
        if (left.getISetType() == FUNDAMENTAL) {
            return left.intersection(right.getRight()).union(left.difference(right.getLeft()));
        }

        ReducedFundamental rLeft = (ReducedFundamental) left;

        // Simplify any funky pranks from user like Rn{1} from both sides
        if (right.getOperation() != SetSymbols.DIFFERENCE) {
            return rLeft.difference(right.simplify());
        }

        if (rLeft.getOperation() != SetSymbols.DIFFERENCE) {
            return rLeft.simplify().difference(right);
        }

        if (rLeft.getLeft().equals(right.getLeft())) {
            // (U \ A) - (U \ B) = B \ A
            return rLeft.getRight().difference(right.getRight());
        }

        // (U1 \ A) - (U2 \ B) = (U1 \ U2) ∪ (B \ A)
        return rLeft.getLeft().difference(right.getLeft()).union(right.getRight().difference(rLeft.getRight()));
    }
}

