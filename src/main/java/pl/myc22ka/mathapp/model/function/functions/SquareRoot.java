package pl.myc22ka.mathapp.model.function.functions;

import lombok.Getter;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.function.FunctionTypes;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

@Getter
public class SquareRoot extends Function {
    private Function function;
    private int degree;

    // Constructor for a random Root Function
    public SquareRoot() {
        super(FunctionTypes.SQUAREROOT);
        generateRandomFunction();
    }

    // Constructor with given function and degree
    public SquareRoot(Function function, int degree) {
        super(FunctionTypes.SQUAREROOT);

        this.function = function;
        this.degree = degree;

        updateExpression();
    }

    @Override
    @NotFullyImplemented
    public void generateRandomFunction() {
        this.degree = 2;
        this.function = new Function(FunctionTypes.LINEAR);

        updateExpression();
    }

    protected void updateExpression() {
        IExpr parsedFunction = function.getSymjaExpression();

        IExpr expr = F.Power(parsedFunction, F.fraction(1, degree));

        setExpressions(expr.toString());
    }
}
