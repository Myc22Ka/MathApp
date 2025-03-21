package pl.myc22ka.mathapp.model;

import lombok.Getter;
import lombok.Setter;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

@Getter
public abstract class Function implements FunctionInterface{
    private static final ExprEvaluator evaluator = new ExprEvaluator();

    protected FunctionTypes type;

    @Setter
    protected char variable;
    protected String function;

    public Function(FunctionTypes type, char variable) {
        this.type = type;
        this.variable = variable;
    }

    public IExpr getRoots() { return evaluator.eval("Solve(" + getFunctionString() + " == 0, " + variable + ")"); }

    public IExpr getDerivative(){ return evaluator.eval("D(" + getFunctionString() + ", " + variable + ")"); }

    public IExpr getRange() { return evaluator.eval("FunctionRange[" + getFunctionString() + ", " + variable + ", y]"); }

    public IExpr getDomain() { return evaluator.eval("FunctionDomain(" + getFunctionString() + ", " + variable + ")"); }

    public IExpr getIntegral() { return evaluator.eval("Integrate(" + getFunctionString() + ", " + variable + ") + C"); }

    public double getFunctionValue(double x) {
        return Double.parseDouble(evaluator.eval(getFunctionString().replace(String.valueOf(variable), String.valueOf(x))).toString());
    }

    public double getFunctionValue(String function, double x) {
        return Double.parseDouble(evaluator.eval(function.replace(String.valueOf(variable), String.valueOf(x))).toString());
    }

    public boolean isPointOnSlope(Point point) {
        var derivative = getDerivative();
        var functionValue = getFunctionValue(point.getX());

        var slopeAtX = getFunctionValue(derivative.toString(), point.getX());

        // Check if the point satisfies the slope equation: y = f(x) + m(x - x0)
        return point.getY() == functionValue + slopeAtX * (variable - point.getX());
    }


    public abstract void generateRandomFunction();

    public abstract String getFunctionString();

    public abstract void generateFunctionFromAnswers(double... answers);
}
