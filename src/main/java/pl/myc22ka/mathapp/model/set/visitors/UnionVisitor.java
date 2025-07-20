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
import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;
import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

/**
 * Visitor for computing the set union (A ∪ B).
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 2025-06-29
 */
@RequiredArgsConstructor
public class UnionVisitor implements SetVisitor<ISet> {
    private final ExprEvaluator evaluator = new ExprEvaluator();
    private final ISet left;

    @Override
    public ISet visitFinite(Finite right) {
        if (left.isEmpty()) return right;

        // Delegate to if not FINITE
        if (left.getISetType() != FINITE) return visitInterval(right.toInterval());

        // Default case: A ∪ B
        IExpr result = evaluator.eval(F.Union(left.getExpression(), right.getExpression()));
        return result.isList() ? new Finite(result) : new Fundamental(EMPTY);
    }

    @Override
    public ISet visitInterval(Interval right) {
        if (left.isEmpty()) return right;

        if(left.getISetType() == FUNDAMENTAL) {
            return visitFundamental((Fundamental) left);
        }

        // General case A ∪ B
        String union = evaluator.eval(F.IntervalUnion(left.toInterval().getExpression(), right.getExpression())).toString();

        Interval result = new Interval(union);

        return SetSymbols.isReal(result.toString()) ? new Fundamental(REAL) : result;
    }

    @Override
    public ISet visitFundamental(Fundamental right) {
        if (left.isEmpty()) return right;
        if (right.isEmpty()) return left;

        if(left.getISetType() != FUNDAMENTAL) return right.toInterval().union(left.toInterval());

        var fundamentalLeft = (Fundamental) left;

        if (right.getLeftSymbol().equals(REAL) || fundamentalLeft.getLeftSymbol().equals(REAL)) return new Fundamental(REAL);

        return new Fundamental(EMPTY);
    }

    @Override
    public ISet visitReducedFundamental(ReducedFundamental right) {
        if (left.isEmpty()) return right;
        if (right.isEmpty()) return left;

        return visitInterval(right.toInterval());
    }
}
