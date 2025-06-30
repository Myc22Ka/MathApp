package pl.myc22ka.mathapp.model.set.visitors;

import lombok.RequiredArgsConstructor;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.Finite;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.sets.ReducedFundamental;

import java.util.List;

import static pl.myc22ka.mathapp.model.set.ISetType.*;
import static pl.myc22ka.mathapp.model.set.SetSymbols.*;

/**
 * Visitor for computing the set union (A ∪ B).
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 2025-06-29
 */
@RequiredArgsConstructor
public class UnionVisitor implements SetVisitor<ISet> {
    private final ExprEvaluator evaluator = new ExprEvaluator();
    private final ISet left;

    @Override
    public ISet visitFinite(Finite right) {
        if (left.isEmpty()) return right;

        // (A ∪ B = B ∪ A)
        if (left.getISetType() == INTERVAL) return right.union(left);

        // Check if REDUCED_FUNDAMENTAL could be simplified
        if (left instanceof ReducedFundamental rLeft) {
            var simplified = rLeft.simplify();
            if (simplified.getISetType() != REDUCED_FUNDAMENTAL) {
                return simplified.union(right);
            }
        }

        // Case for FUNDAMENTAL and REDUCED_FUNDAMENTAL
        if (left.getISetType() == FUNDAMENTAL || left.getISetType() == REDUCED_FUNDAMENTAL) {
            List<IExpr> excludedElements = SetUtils.getExcludedElements(left, right);

            // First check if it is REDUCED_FUNDAMENTAL
            if (left instanceof ReducedFundamental rLeft) {
                ISet innerDifference = rLeft.getRight().difference(right);

                // Write symbolic result
                if (innerDifference.getISetType() == REDUCED_FUNDAMENTAL) {
                    innerDifference = new ReducedFundamental(
                            rLeft.getRight(), DIFFERENCE, right, true
                    );
                }

                // (A \ B) ∪ B = A → return A
                if (innerDifference.isEmpty()) {
                    return new Fundamental(rLeft.getLeft().toString());
                }

                // (A \ B) ∪ C → keep it reduced
                return new ReducedFundamental(
                        rLeft.getLeft(), rLeft.getOperation(), innerDifference, false
                );
            }

            // If empty return left
            if (excludedElements.isEmpty()) return left;

            // A ∪ B = A ∪ {new elements}
            return new ReducedFundamental(left, UNION, new Finite(excludedElements), false);
        }

        // Default case: A ∪ B
        IExpr result = evaluator.eval(F.Union(left.getExpression(), right.getExpression()));
        return result.isList() ? new Finite(result) : new Fundamental(SetSymbols.EMPTY);
    }

    @Override
    public ISet visitInterval(Interval right) {
        if (left.isEmpty()) return right;

        // If left is finite, convert it to interval
        ISet normalizedLeft = (left.getISetType() == FINITE) ? left.toInterval() : left;

        if (normalizedLeft instanceof Fundamental fLeft) {
            // ℝ ∪ A = ℝ
            if (fLeft.getLeftSymbol().equals(SetSymbols.REAL)) return fLeft;

            // General case: A ∪ B
            return new ReducedFundamental(fLeft, UNION, right, false);
        }

        // (A \ B) ∪ C = (A ∩ B') ∪ C = (B' ∪ C) ∩ (C ∪ A)
        if (left.getISetType() == REDUCED_FUNDAMENTAL) {
            var rLeft = (ReducedFundamental) left;

            // If left is REDUCED_FUNDAMENTAL and both are FUNDAMENTAL like R\Z then return symbolic union
            if (rLeft.getLeft().getISetType() == FUNDAMENTAL && rLeft.getRight().getISetType() == FUNDAMENTAL) {
                return new ReducedFundamental(
                        right,
                        UNION,
                        new ReducedFundamental(rLeft.getLeft(), rLeft.getOperation(), rLeft.getRight(), true),
                        false
                );
            }

            var complement = rLeft.getRight().complement(new Fundamental(REAL)); // B'

            var result = complement.union(right).intersection(right.union(rLeft.getLeft()));

            if (SetSymbols.isReal(result.toString())) {
                return new Fundamental(REAL);
            }

            return result;
        }

        // General case A ∪ B
        IExpr union = evaluator.eval(F.IntervalUnion(normalizedLeft.getExpression(), right.getExpression()));
        return new Interval(union.toString());
    }

    @Override
    public ISet visitFundamental(Fundamental right) {
        if (left.isEmpty()) return right;
        if (right.isEmpty()) return left;

        if (left instanceof Finite finite){
           if (SetUtils.isCovered(finite, right)) {
              return right;
           }

           return new ReducedFundamental(left, UNION, right, false);
        }

        // Two fundamental sets: A ∪ B
        if (left instanceof Fundamental fLeft) {
            return SetUtils.resolveFundamentalUnion(fLeft, right);
        }

        // (A ∪ B = B ∪ A)
        if (left.getISetType() == FUNDAMENTAL || left.getISetType() == INTERVAL) {
            return right.union(left);
        }

        ReducedFundamental rLeft = (ReducedFundamental) left;
        Fundamental fLeft = (Fundamental) rLeft.getLeft();

        if (right.getLeftSymbol().isSubsetOf(fLeft.getLeftSymbol())) {

            // (A \ B) ∪ B = A
            if (rLeft.getOperation() == DIFFERENCE &&
                    rLeft.getRight() instanceof Finite finite &&
                    SetUtils.isCovered(finite, right)) {
                return fLeft;
            }

            // (A \ B) ∪ C — no simplification
            return right;
        }

        // C ⊇ A: (A \ B) ∪ C = C \ B
        if (fLeft.getLeftSymbol().isSubsetOf(right.getLeftSymbol())) {
            return new ReducedFundamental(right, DIFFERENCE, rLeft.getRight(), false);
        }

        // Default: (A \ B) ∪ C
        return new ReducedFundamental(fLeft, UNION, right, false);
    }

    @Override
    public ISet visitReducedFundamental(ReducedFundamental right) {
        // (A \ B) ∪ B = A
        if (left.getISetType() != REDUCED_FUNDAMENTAL) {
            return right.union(left);
        }

        ReducedFundamental rLeft = (ReducedFundamental) left;

        // Simplify any funky pranks from user like Rn{1} from both sides
        if (right.getOperation() != DIFFERENCE) {
            return right.simplify().union(left);
        }

        if (rLeft.getOperation() != DIFFERENCE) {
            return rLeft.simplify().union(right);
        }

        // (A \ B) ∪ (C \ D) = (A ∪ C) \ (B ∪ D)
        ISet newLeft = right.getLeft().union(rLeft.getLeft());

        if (right.getRight() instanceof Finite tempRight) {
            List<IExpr> excluded = SetUtils.getExcludedElements(rLeft, tempRight);

            if (excluded.isEmpty()) return newLeft;

            return new ReducedFundamental(newLeft, DIFFERENCE, new Finite(excluded), false);
        }


        return new Fundamental(SetSymbols.EMPTY);
    }
}
