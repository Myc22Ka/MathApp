package pl.myc22ka.mathapp.model;

import lombok.Getter;
import lombok.Setter;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.utils.MathUtils;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;
import pl.myc22ka.mathapp.utils.functions.ConditionRoots;
import pl.myc22ka.mathapp.utils.functions.Point;

import java.util.List;

@Getter
public abstract class Expression implements FunctionInterface {
    private final ExprEvaluator evaluator = new ExprEvaluator();

    protected FunctionTypes type;
    protected ISymbol variable;

    @Setter
    protected IExpr expression;

    public Expression(FunctionTypes type, ISymbol variable) {
        this(type, variable, null);
    }

    public Expression(FunctionTypes type, ISymbol variable, IExpr expression) {
        this.type = type;
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public List<IExpr> getRealRoots() {
        var expr = evaluator.eval(F.Solve(F.Equal(expression, F.C0), variable).toString());

        return MathUtils.getRootsFromExpr(expr).stream().filter(root -> !root.isComplex()).toList();
    }

    @Override
    public List<ConditionRoots> getRealConditionRoots(ISymbol symbol) {
        var expr = evaluator.eval(F.Solve(F.Equal(expression, F.C0), variable, F.Rule(F.GenerateConditions, F.True)).toString());

        expr = evaluator.eval(F.ReplaceAll(expr, F.Rule(F.C, symbol)));

        List<ConditionRoots> rootsAndConditions = MathUtils.getConditionsRootsFromExpr(expr);

        return rootsAndConditions.stream().filter(rootCondition -> !rootCondition.root().toString().contains("I")).toList();
    }

    @NotFullyImplemented
    @Override
    public List<IExpr> getRealRoots(double min, double max) {
        var expr = evaluator.eval(F.Solve(F.Equal(expression, F.C0), variable).toString());

        List<IExpr> allRoots = MathUtils.getRootsFromExpr(expr).stream()
                .filter(root -> !root.toString().contains("I"))
                .toList();

        return allRoots.stream()
                .filter(root -> {
                    if (root.isNumber()) {
                        double value = root.evalf();
                        return value >= min && value <= max;
                    }
                    return false;
                })
                .toList();
    }

    @Override
    public List<IExpr> getAllRoots() {
        var expr = evaluator.eval(F.Solve(F.Equal(expression, F.C0), variable).toString());

        return MathUtils.getRootsFromExpr(expr);
    }

    @Override
    public IExpr getDerivative() { return evaluator.eval(F.D(expression, variable).toString()); }

    @Override
    public IExpr getRange() { return evaluator.eval(F.FunctionRange(expression, variable, F.y).toString()); }

    @Override
    public IExpr getDomain() { return evaluator.eval(F.FunctionDomain(expression, variable).toString()); }

    @Override
    public IExpr getIntegral() { return evaluator.eval(F.Integrate(expression, variable).plus(F.C).toString()); }

    @Override
    public IExpr getFactoredForm() { return evaluator.eval(F.Factor(expression).toString()); }

    @Override
    public IExpr getFunctionValue(IExpr x) { return evaluator.eval(expression.replaceAll(F.Rule(variable, x)).toString()); }

    @Override
    public IExpr getFunctionValue(String function, IExpr x) { return evaluator.eval(F.eval(function).replaceAll(F.Rule(variable, x)).toString()); }

    @Override
    public boolean isPointOnSlope(Point point) { return getFunctionValue(point.getX()).equals(point.getY()); }

    @Override
    public String toString() { return expression != null ? expression.toString() : "undefined"; }
}
