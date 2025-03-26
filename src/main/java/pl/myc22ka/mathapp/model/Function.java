package pl.myc22ka.mathapp.model;

import lombok.Getter;
import lombok.Setter;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.ISymbol;

import java.util.List;

@Getter
public abstract class Function implements FunctionInterface {
    protected FunctionTypes type;

    @Setter
    protected ISymbol variable;

    protected IExpr expression;

    public Function(FunctionTypes type, ISymbol variable) {
        this.type = type;
        this.variable = variable;
    }

    /**
     * @return An IExpr representing the list of real roots of the equation.
     */
    public IExpr getRealRoots() {
        return F.Solve(F.Equal(expression, F.C0), variable).eval();
    }

    /**
     * @return An IExpr representing the list of all roots of the equation.
     */
    public IExpr getAllRoots() {
        return F.Solve(F.Equal(expression, F.C0), variable, F.List()).eval();
    }

    /**
     * Returns the derivative of the function.
     */
    public IExpr getDerivative() {
        return F.D(expression, variable).eval();
    }

    /**
     * Returns the range of the function.
     */
    public IExpr getRange() {
        return F.FunctionRange(expression, variable, F.y).eval();
    }

    /**
     * Returns the domain of the function.
     */
    public IExpr getDomain() {
        return F.FunctionDomain(expression, variable).eval();
    }

    /**
     * Returns the integral of the function.
     */
    public IExpr getIntegral() {
        return F.Integrate(expression, variable).plus(F.C).eval();
    }

    public IExpr getFactoredForm() {
        return F.Factor(expression).eval();
    }

    /**
     * Evaluates the function at a given x value.
     */
    public IExpr getFunctionValue(IExpr x) {
        return expression.replaceAll(F.Rule(variable, x)).eval();
    }

    /**
     * Evaluates a provided function at a given x value.
     */
    public IExpr getFunctionValue(String function, IExpr x) {
        return F.eval(function).replaceAll(F.Rule(variable, x)).eval();
    }

    public boolean isPointOnSlope(Point point) {
        IExpr functionValue = getFunctionValue(point.getX());

        return false;
    }

    /**
     * Provides the string representation of the function
     */
    @Override
    public String toString() {
        return expression.toString();
    }

    /**
     * Sets the expression of the function
     */
    protected void setExpression(IExpr expr) {
        this.expression = expr;
    }

    /**
     * Sets the expression of the function from a string
     */
    protected void setExpressionFromString(String exprString) {
        this.expression = F.eval(exprString);
    }

    public abstract void generateRandomFunction();

    public abstract void generateFunctionFromAnswers(List<IExpr> answers);
}
