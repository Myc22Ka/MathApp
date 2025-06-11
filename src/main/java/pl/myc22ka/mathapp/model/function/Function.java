package pl.myc22ka.mathapp.model.function;

import lombok.Getter;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.exceptions.FunctionErrorMessages;
import pl.myc22ka.mathapp.exceptions.FunctionException;
import pl.myc22ka.mathapp.model.function.functions.Constant;
import pl.myc22ka.mathapp.utils.math.MathUtils;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;
import pl.myc22ka.mathapp.utils.annotations.NotTested;
import pl.myc22ka.mathapp.utils.functions.ConditionRoots;
import pl.myc22ka.mathapp.utils.functions.Point;

import java.util.List;

@Getter
public class Function implements FunctionInterface {
    protected final ExprEvaluator evaluator = new ExprEvaluator();
    private final FunctionTypes type;
    protected final ISymbol variable;
    protected String rawExpression;
    protected IExpr symjaExpression;

    public Function(String function) {
        this(FunctionTypes.FUNCTION, MathUtils.detectFirstVariable(function), function);
    }

    public Function(IExpr function) {
        this(FunctionTypes.FUNCTION, MathUtils.detectFirstVariable(function), function);
    }

    public Function(FunctionTypes type) {
        this(type, F.x);
    }

    public Function(FunctionTypes type, ISymbol variable) {
        this.type = type;
        this.variable = variable;
    }

    public Function(FunctionTypes type, ISymbol variable, String rawExpression) {
        this(type, variable);
        setExpressions(rawExpression);
    }

    public Function(FunctionTypes type, ISymbol variable, IExpr symjaExpression) {
        this(type, variable);
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

    @NotTested
    @NotFullyImplemented
    @Override
    public List<IExpr> getRealRoots(double min, double max) {
        var expr = evaluator.eval(F.Solve(F.Equal(symjaExpression, F.C0), variable).toString());
        List<IExpr> allRoots = MathUtils.getRootsFromExpr(expr).stream()
                .filter(root -> !root.toString().contains("I"))
                .toList();
        return allRoots.stream()
                .filter(root -> root.isNumber() && root.evalf() >= min && root.evalf() <= max)
                .toList();
    }

    @Override
    public List<IExpr> getAllRoots() {
        var expr = evaluator.eval(F.Solve(F.Equal(symjaExpression, F.C0), variable).toString());
        return MathUtils.getRootsFromExpr(expr);
    }

    @NotTested
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

    @NotFullyImplemented
    public void generateRandomFunction() {
        rawExpression = "Cos(x)*Sin(3*x)";
        // TO DO...
    }

    @NotFullyImplemented
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        // TO DO...
    }

    @Override
    public final Function plus(Function other) {
        return new Function(F.Plus(symjaExpression, F.Parenthesis(other.symjaExpression)));
    }

    @Override
    public final Function minus(Function other) {
        return new Function(F.Subtract(symjaExpression, F.Parenthesis(other.symjaExpression)));
    }

    @Override
    public final Function times(Function other) {
        return new Function(F.Times(symjaExpression, other.symjaExpression));
    }

    @Override
    public final Function divide(Function other) {
        if (other instanceof Constant c) {
            if (c.getSymjaExpression().isZero()) {
                throw new FunctionException(FunctionErrorMessages.ILLOGICAL_MATH_OPERATION);
            }
        }

        return new Function(F.Times(symjaExpression, F.Power(other.symjaExpression, F.CN1)));
    }

    @Override
    public final Function composition(Function other) {
        if (this.getType() == FunctionTypes.CONSTANT && other.getType() == FunctionTypes.CONSTANT) {
            throw new FunctionException(FunctionErrorMessages.ILLOGICAL_MATH_OPERATION);
        }

        var rule = F.Rule(evaluator.eval(getVariable() + "_"), evaluator.eval("HoldForm[" + rawExpression + "]"));
        return new Function(other.symjaExpression.replaceAll(rule));
    }

    @Override
    public String toString() {
        return rawExpression != null && !rawExpression.isBlank() ? rawExpression : F.NIL.toString();
    }
}
