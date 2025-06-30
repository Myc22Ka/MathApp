package pl.myc22ka.mathapp.model.set.sets;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.ISetType;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.visitors.SetVisitor;
import pl.myc22ka.mathapp.model.set.visitors.DifferenceVisitor;
import pl.myc22ka.mathapp.model.set.visitors.IntersectionVisitor;
import pl.myc22ka.mathapp.model.set.visitors.UnionVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Mathematical finite set {1,2,3,4}.
 *
 * @author Myc22Ka
 * @version 1.0
 * @since 2025‑06‑19
 */
public class Finite implements ISet {
    private final ExprEvaluator evaluator = new ExprEvaluator();
    private IExpr expression;

    public Finite(IExpr start, IExpr end, IExpr step) {
        expression = evaluator.eval(F.Range(start, end, step));

        if (!expression.isList()) {
            throw new IllegalArgumentException("Range[start, end] did not produce a finite list.");
        }
    }

    public Finite(@NotNull IExpr expr) {
        this.expression = expr;
    }

    public Finite(String exprString) {
        this.expression = evaluator.eval(exprString);

        if (!expression.isList()) {
            throw new IllegalArgumentException("Provided string does not evaluate to a finite list: " + exprString);
        }
    }

    public Finite(List<IExpr> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("Element list cannot be null or empty.");
        }

        this.expression = F.List(elements.toArray(new IExpr[0]));
    }

    public @NotNull List<IExpr> exprToList() {
        IAST ast = (IAST) expression;
        List<IExpr> elements = new ArrayList<>();

        for (int i = 1; i < ast.size(); i++) {
            elements.add(ast.get(i));
        }

        return elements;
    }

    @Override
    public Interval toInterval() {
        var list = exprToList();
        List<IExpr> intervals = new ArrayList<>();

        for (var element : list) {
            IExpr intervalData = evaluator.eval(String.format(
                    "IntervalData({%s,%s,%s,%s})",
                    element, BoundType.CLOSED, BoundType.CLOSED, element
            ));
            intervals.add(intervalData);
        }

        IExpr unionExpr = F.IntervalUnion(intervals.toArray(new IExpr[0]));
        return new Interval(unionExpr.toString());
    }

    @Override
    public <T> T accept(@NotNull SetVisitor<T> visitor) {
        return visitor.visitFinite(this);
    }

    @Override
    public @NotNull IExpr getExpression() {
        return expression;
    }

    @Override
    public @NotNull ISetType getISetType() {
        return ISetType.FINITE;
    }

    @Override
    public boolean contains(@NotNull String x) {
        return evaluator.eval("ContainsAny[" + expression + ", " + F.List(x) + "]").isTrue();
    }

    @Override
    public void remove(@NotNull String element) {
        expression = evaluator.eval("Complement(" + expression + ",{" + element + "})");
    }

    @Override
    public Integer size() {
        return expression.size() - 1;
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
        return expression.toString().equals("{}") ? SetSymbols.EMPTY.toString() : expression.toString();
    }
}
