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

import static pl.myc22ka.mathapp.model.set.ISetType.FINITE;
import static pl.myc22ka.mathapp.model.set.ISetType.FUNDAMENTAL;
import static pl.myc22ka.mathapp.model.set.SetSymbols.DIFFERENCE;
import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;

/**
 * Visitor for computing the set difference (A ∖ B).
 *
 * @author Myc22Ka
 * @version 1.0.3
 * @since 2025 -06-19
 */
@RequiredArgsConstructor
public class DifferenceVisitor implements SetVisitor<ISet> {
    private final ISet left;
    private final ExprEvaluator evaluator = new ExprEvaluator();

    @Override
    public ISet visitFinite(Finite right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        if (left.getISetType() == FUNDAMENTAL) {
            return new ReducedFundamental(left, DIFFERENCE, right);
        }

        if (left instanceof ReducedFundamental rLeft) {

            var union = rLeft.getRight().union(right);

            if (union.getISetType() == FINITE && rLeft.getOperation() == DIFFERENCE)
                return new ReducedFundamental(rLeft.getLeft(), DIFFERENCE, rLeft.getRight().union(right));

            var simplified = rLeft.simplify();

            return simplified.difference(right);
        }

        // Delegate if not finite
        if (left.getISetType() != FINITE) {
            return visitInterval(right.toInterval());
        }

        IExpr result = evaluator.eval(F.Complement(left.getExpression(), right.getExpression()));

        // Default case: A \ B
        return result.isList() ? new Finite(result) : new Fundamental(EMPTY);
    }

    @Override
    public ISet visitInterval(Interval right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        ISet normalizedLeft = left.toInterval();

        String difference = evaluator.eval(
                F.IntervalComplement(normalizedLeft.getExpression(), right.getExpression())
        ).toString();

        return new Interval(difference);
    }

    @Override
    public ISet visitFundamental(Fundamental right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        if (left.toString().equals(right.toString())) return new Fundamental(SetSymbols.EMPTY);

        return visitInterval(right.toInterval());
    }

    @Override
    public ISet visitReducedFundamental(ReducedFundamental right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        if (left.toString().equals(right.getLeftSymbol().toString())) return right.getRight();

        if (left.getISetType() == FUNDAMENTAL) {
            return right.getRight();
        }

        if (right.getOperation() != DIFFERENCE) {
            return left.difference(right.simplify()).toInterval().shorten();
        }

        return right.getRight().intersection(left);
    }
}

