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

import static pl.myc22ka.mathapp.model.set.ISetType.*;
import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;
import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

/**
 * Visitor for computing the set intersection (A ∩ B).
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 2025 -06-24
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

        return SetSymbols.isReal(result.toString()) ? new Fundamental(REAL) : result.shorten();
    }

    @Override
    public ISet visitFundamental(Fundamental right) {
        if (right.isEmpty() || left.isEmpty()) return new Fundamental(SetSymbols.EMPTY);

        if (left.getISetType() != FUNDAMENTAL) return right.intersection(left);

        var fLeft = (Fundamental) left;

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

    @Override
    public ISet visitReducedFundamental(ReducedFundamental right) {
        if (left.isEmpty() || right.isEmpty()) return new Fundamental(EMPTY);

        return visitInterval(right.toInterval());
    }
}