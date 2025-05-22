package pl.myc22ka.mathapp.model;

import lombok.Getter;
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
    protected final ExprEvaluator evaluator = new ExprEvaluator();
    protected FunctionTypes type;

    protected ISymbol variable;

    protected String rawExpression;

    protected IExpr symjaExpression;

    public Expression(FunctionTypes type) {
        this.type = type;
        this.variable = F.x; // getVariables()
    }

    public Expression(FunctionTypes type, String rawExpression) {
        this.type = type;
        this.variable = F.x; // getVariables()

        setExpressions(rawExpression);
    }

    public Expression(FunctionTypes type, IExpr symjaExpression) {
        this.type = type;
        this.variable = F.x; // getVariables()

        setExpressions(symjaExpression);
    }

    public void setExpressions(String rawExpression) {
        this.rawExpression = rawExpression;
        this.symjaExpression = evaluator.parse(rawExpression);
    }

    public void setExpressions(IExpr symjaExpression) {
        this.rawExpression = symjaExpression.toString();
        this.symjaExpression = symjaExpression;
    }

    @Override
    public List<IExpr> getRealRoots() {
        var expr = evaluator.eval(F.Solve(F.Equal(symjaExpression, F.C0), variable).toString());

        return MathUtils.getRootsFromExpr(expr).stream().filter(root -> !root.isComplex()).toList();
    }

    @Override
    public List<ConditionRoots> getRealConditionRoots(ISymbol symbol) {
        var expr = evaluator
                .eval(F.Solve(F.Equal(symjaExpression, F.C0), variable, F.Rule(F.GenerateConditions, F.True))
                        .toString());

        expr = evaluator.eval(F.ReplaceAll(expr, F.Rule(F.C, symbol)));

        List<ConditionRoots> rootsAndConditions = MathUtils.getConditionsRootsFromExpr(expr);

        return rootsAndConditions.stream().filter(rootCondition -> !rootCondition.root().toString().contains("I"))
                .toList();
    }

    @NotFullyImplemented
    @Override
    public List<IExpr> getRealRoots(double min, double max) {
        var expr = evaluator.eval(F.Solve(F.Equal(symjaExpression, F.C0), variable).toString());

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
        var expr = evaluator.eval(F.Solve(F.Equal(symjaExpression, F.C0), variable).toString());

        return MathUtils.getRootsFromExpr(expr);
    }

    @Override
    public IExpr getVaraibles() {
        System.out.println(evaluator.eval(F.Variables(symjaExpression)));

        return null;
    }

    @Override
    public IExpr getDerivative() {
        return evaluator.eval(F.D(symjaExpression, variable).toString());
    }

    @Override
    public IExpr getRange() {
        return evaluator.eval(F.FunctionRange(symjaExpression, variable, F.y).toString());
    }

    @Override
    public IExpr getDomain() {
        return evaluator.eval(F.FunctionDomain(symjaExpression, variable).toString());
    }

    @Override
    public IExpr getIntegral() {
        return evaluator.eval(F.Integrate(symjaExpression, variable).plus(F.C).toString());
    }

    @Override
    public IExpr getFactoredForm() {
        return evaluator.eval(F.Factor(symjaExpression).toString());
    }

    @Override
    public IExpr getFunctionValue(IExpr x) {
        return evaluator.eval(symjaExpression.replaceAll(F.Rule(variable, x)).toString());
    }

    @Override
    public IExpr getFunctionValue(String function, IExpr x) {
        return evaluator.eval(F.eval(function).replaceAll(F.Rule(variable, x)).toString());
    }

    @Override
    public boolean isPointOnSlope(Point point) {
        return getFunctionValue(point.getX()).equals(point.getY());
    }

    @Override
    public String toString() {
        return rawExpression != null && !rawExpression.isBlank() ? rawExpression : F.NIL.toString();
    }
}
