package pl.myc22ka.mathapp.model.set.visitors;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.ISetType;
import pl.myc22ka.mathapp.model.set.Set;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.Finite;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.sets.ReducedFundamental;

import java.util.ArrayList;
import java.util.List;

import static pl.myc22ka.mathapp.model.set.ISetType.FINITE;
import static pl.myc22ka.mathapp.model.set.ISetType.REDUCED_FUNDAMENTAL;
import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;
import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

/**
 * Visitor for computing the set intersection (A ∩ B).
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 2025-06-24
 */
@RequiredArgsConstructor
public class IntersectionVisitor implements SetVisitor<ISet> {
    private final ExprEvaluator evaluator = new ExprEvaluator();
    private final ISet left;

    @Override
    public ISet visitFinite(Finite right) {
        if (right.isEmpty() || left.isEmpty()) return new Fundamental(SetSymbols.EMPTY);

        // Delegate to Interval Case
        if (left.getISetType() != FINITE) return visitInterval(right.toInterval());

        IExpr result = evaluator.eval(F.Intersection(left.getExpression(), right.getExpression()));

        return result.isList() ? new Finite(result) : new Fundamental(EMPTY);
    }

    @Override
    public ISet visitInterval(Interval right) {
        if (right.isEmpty() || left.isEmpty()) return new Fundamental(SetSymbols.EMPTY);

        String intersection = evaluator.eval(F.IntervalIntersection(left.toInterval().getExpression(), right.getExpression())).toString();

        Interval result = new Interval(intersection);

        return SetSymbols.isReal(result.toString()) ? new Fundamental(REAL) : result;
    }

    @Override
    public ISet visitFundamental(Fundamental right) {
        if (right.isEmpty() || left.isEmpty()) return new Fundamental(SetSymbols.EMPTY);

        if (left instanceof Finite || left instanceof Interval) {
            return right.intersection(left);
        }

        if (left instanceof Fundamental fLeft) {
            if (fLeft.getLeftSymbol().equals(REAL)) {
                return right;
            }
            if (right.getLeftSymbol().equals(REAL)) {
                return fLeft;
            }

            if(right.getLeftSymbol().equals(fLeft.getLeftSymbol())) {
                return fLeft;
            }

            return new Fundamental(SetSymbols.EMPTY);
        }

       return null;
    }

    @Override
    public ISet visitReducedFundamental(ReducedFundamental right) {
        if (left.getISetType() != REDUCED_FUNDAMENTAL) {
            // A ∩ (B ∖ C) = (A ∩ B) ∖ C
            if (left instanceof Fundamental || left instanceof Interval) {
                ISet baseIntersection = left.intersection(right.getLeft());

                if (baseIntersection.isEmpty()) return new Fundamental(SetSymbols.EMPTY);

                return baseIntersection.difference(right.getRight());
            }

            return right.intersection(left); // fallback
        }

        ReducedFundamental rLeft = (ReducedFundamental) left;

        // Simplify any funky pranks from user like Rn{1} from both sides
        if (right.getOperation() != SetSymbols.DIFFERENCE) {
            return right.simplify().intersection(left);
        }

        if (rLeft.getOperation() != SetSymbols.DIFFERENCE) {
            return rLeft.simplify().intersection(right);
        }

        // (A ∖ B) ∩ (C ∖ D) = (A ∩ C) ∖ (B ∪ D)
        ISet baseIntersection = rLeft.getLeft().intersection(right.getLeft());

        if (baseIntersection.isEmpty()) {
            return new Fundamental(SetSymbols.EMPTY);
        }

        ISet sum = rLeft.getRight().union(right.getRight());

        return baseIntersection.difference(sum);
    }
}