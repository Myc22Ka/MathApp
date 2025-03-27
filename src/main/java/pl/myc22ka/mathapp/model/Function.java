package pl.myc22ka.mathapp.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.utils.MathUtils;

import java.util.List;

@Getter
public abstract class Function implements FunctionInterface {
    protected FunctionTypes type;

    protected ISymbol variable;
    protected IExpr expression;

    public Function(FunctionTypes type, ISymbol variable) {
        this.type = type;
        this.variable = variable;
    }

    @Override
    public List<IExpr> getRealRoots() {
        var expr = F.Solve(F.Equal(expression, F.C0), variable).eval();

        return MathUtils.extractRootsFromExpr(expr).stream().filter(root -> !root.toString().contains("I")).toList();
    }

    @Override
    public List<IExpr> getAllRoots() {
        var expr = F.Solve(F.Equal(expression, F.C0), variable).eval();

        return MathUtils.extractRootsFromExpr(expr);
    }

    @Override
    public IExpr getDerivative() { return F.D(expression, variable).eval(); }

    @Override
    public IExpr getRange() { return F.FunctionRange(expression, variable, F.y).eval(); }

    @Override
    public IExpr getDomain() { return F.FunctionDomain(expression, variable).eval(); }

    @Override
    public IExpr getIntegral() { return F.Integrate(expression, variable).plus(F.C).eval(); }

    @Override
    public IExpr getFactoredForm() { return F.Factor(expression).eval(); }

    @Override
    public IExpr getFunctionValue(IExpr x) { return expression.replaceAll(F.Rule(variable, x)).eval(); }

    @Override
    public IExpr getFunctionValue(String function, IExpr x) { return F.eval(function).replaceAll(F.Rule(variable, x)).eval(); }

    @Override
    public boolean isPointOnSlope(Point point) { return getFunctionValue(point.getX()).equals(point.getY()); }

    @Override
    public String toString() { return expression.toString(); }

    protected void setExpression(IExpr expr) { this.expression = expr; }


    protected abstract void updateExpression();

    public abstract void generateRandomFunction();

    public abstract void generateFunctionFromAnswers(List<IExpr> answers);
}
