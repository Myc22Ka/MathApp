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

import static pl.myc22ka.mathapp.model.set.ISetType.*;
import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;

/**
 * Visitor for computing the set difference (A ∖ B).
 *
 * @author Myc22Ka
 * @version 1.0.1
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

        ISet normalizedLeft =  left.toInterval();

        IExpr expr = evaluator.eval(
                F.IntervalComplement(normalizedLeft.getExpression(), right.getExpression())
        );

        return new Interval(expr.toString());
    }

    @Override
    public ISet visitFundamental(Fundamental right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        if (left.toString().equals(right.toString())) return new Fundamental(SetSymbols.EMPTY);

        return visitInterval(left.toInterval());
    }

    @Override
    public ISet visitReducedFundamental(ReducedFundamental right) {
        if (left.isEmpty() || right.isEmpty()) return left;

        if (left.toString().equals(right.getLeftSymbol().toString())) return right.getRight();

        return visitInterval(left.toInterval());
    }
}

