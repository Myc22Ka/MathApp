package pl.myc22ka.mathapp.model.function.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.function.FunctionType;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.utils.math.MathUtils;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

public class Exponential extends Function {
    private IExpr coefficient;
    private Function base;
    private Function exponent;

    public Exponential() {
        super(FunctionType.EXPONENTIAL);

        generateRandomFunction();
    }

    public Exponential(Function base, IExpr coefficient, Function exponent) {
        super(FunctionType.EXPONENTIAL);
        this.base = base;
        this.exponent = exponent;
        this.coefficient = coefficient;

        updateExpression();
    }

    public Exponential(String rawExpression) {
        super(FunctionType.EXPONENTIAL, MathUtils.detectFirstVariable(rawExpression), rawExpression);
    }

    @NotFullyImplemented
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
