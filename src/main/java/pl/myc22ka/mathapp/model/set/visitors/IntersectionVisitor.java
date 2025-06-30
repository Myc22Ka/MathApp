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

import static pl.myc22ka.mathapp.model.set.ISetType.REDUCED_FUNDAMENTAL;

/**
 * Visitor for computing the set intersection (A ∩ B).
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 2025-06-24
 */
@RequiredArgsConstructor
public class IntersectionVisitor implements SetVisitor<ISet> {
    private final ExprEvaluator evaluator = new ExprEvaluator();
    private final ISet left;

    @Override
    public ISet visitFinite(Finite right) {
        if (right.isEmpty() || left.isEmpty()) return new Fundamental(SetSymbols.EMPTY);

        if (left instanceof Interval) {
            return right.intersection(left);
        }

        if (left instanceof ReducedFundamental rLeft) {
            var simplified = rLeft.simplify();

            if(!(simplified instanceof ReducedFundamental))
                return simplified.intersection(right);
        }

        if (left instanceof Fundamental || left instanceof ReducedFundamental) {
            List<IExpr> result = new ArrayList<>();

            for (var element : right.exprToList()) {
                if (left.contains(element.toString())) {
                    result.add(element);
                }
            }

            if (result.isEmpty()) return new Fundamental(SetSymbols.EMPTY);

            return new Finite(result);
        }

        IExpr result = evaluator.eval(F.Intersection(left.getExpression(), right.getExpression()));

        if (result.isList()) return new Finite(result);

        return new Fundamental(SetSymbols.EMPTY);
    }

    @Override
    public ISet visitInterval(Interval right) {
        if (right.isEmpty() || left.isEmpty()) return new Fundamental(SetSymbols.EMPTY);

        ISet normalizedLeft = (left instanceof Finite) ? left.toInterval() : left;

        if (normalizedLeft instanceof Fundamental fLeft) {
            if (fLeft.getLeftSymbol().equals(SetSymbols.REAL)) {
                return right;
            }

            return new ReducedFundamental(fLeft, SetSymbols.INTERSECTION, right, false);
        }

        IExpr intersection = evaluator.eval(F.IntervalIntersection(normalizedLeft.getExpression(), right.getExpression()));
        return new Interval(intersection.toString());
    }

    @Override
    public ISet visitFundamental(Fundamental right) {
        if (right.isEmpty() || left.isEmpty()) return new Fundamental(SetSymbols.EMPTY);

        if (left instanceof Finite || left instanceof Interval) {
            return right.intersection(left);
        }

        if (left instanceof Fundamental fLeft) {
            if (right.getLeftSymbol().isSubsetOf(fLeft.getLeftSymbol())) {
                return right;
            }
            if (fLeft.getLeftSymbol().isSubsetOf(right.getLeftSymbol())) {
                return fLeft;
            }

            return new Fundamental(SetSymbols.EMPTY);
        }

        // left instanceof ReducedFundamental
        ReducedFundamental rLeft = (ReducedFundamental) left;
        Fundamental fLeft = (Fundamental) rLeft.getLeft();

        // Jeśli right jest podzbiorem bazowego zbioru z left
        if (right.getLeftSymbol().isSubsetOf(fLeft.getLeftSymbol())) {
            if (rLeft.getOperation() == SetSymbols.DIFFERENCE) {
                // right ∩ (base \ excluded) = right \ (right ∩ excluded)
                ISet rightIntersectionExcluded = right.intersection(rLeft.getRight());
                return right.difference(rightIntersectionExcluded);
            }
            if (rLeft.getOperation() == SetSymbols.UNION) {
                // right ∩ (base ∪ other) = right (jeśli right ⊆ base)
                return right;
            }
            if (rLeft.getOperation() == SetSymbols.INTERSECTION) {
                // right ∩ (base ∩ other) = right ∩ other (jeśli right ⊆ base)
                return right.intersection(rLeft.getRight());
            }
        }

        // Jeśli bazowy zbiór z left jest podzbiorem right
        if (fLeft.getLeftSymbol().isSubsetOf(right.getLeftSymbol())) {
            if (rLeft.getOperation() == SetSymbols.DIFFERENCE) {
                // (base \ excluded) ∩ right = base \ excluded (jeśli base ⊆ right)
                return rLeft;
            }
            if (rLeft.getOperation() == SetSymbols.UNION) {
                // (base ∪ other) ∩ right = base ∪ (other ∩ right)
                return fLeft.union(rLeft.getRight().intersection(right));
            }
            if (rLeft.getOperation() == SetSymbols.INTERSECTION) {
                // (base ∩ other) ∩ right = base ∩ other (jeśli base ⊆ right)
                return rLeft;
            }
        }

        // Różne zbiory
        return new ReducedFundamental(fLeft, SetSymbols.INTERSECTION, right, false);
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