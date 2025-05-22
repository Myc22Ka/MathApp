package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.FunctionTypes;

public class Exponential extends Function {
    private IExpr coefficient;
    private Function base;
    private Function exponent;

    public Exponential() {
        super(FunctionTypes.EXPONENTIAL);

        generateRandomFunction();
    }

    public Exponential(Function base, IExpr coefficient, Function exponent) {
        super(FunctionTypes.EXPONENTIAL);
        this.base = base;
        this.exponent = exponent;
        this.coefficient = coefficient;

        updateExpression();
    }

    @Override
    public void generateRandomFunction() {
        base = new Function(F.e); // Natural base e
        exponent = new Function(F.ZZ(1)); // Default exponent
        coefficient = F.ZZ(1);

        updateExpression();
    }

    protected void updateExpression() {
        var pbase = base.getSymjaExpression();
        var pexponent = exponent.getSymjaExpression();

        setExpressions(F.Times(coefficient, F.Power(pbase, pexponent)).toString());
    }

    @Override
    public IExpr getDomain() {
        return F.True;
    }
}
