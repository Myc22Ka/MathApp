package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

public class Absolute extends Function {
    private Function base;

    public Absolute() {
        super(FunctionTypes.ABSOLUTE);

        generateRandomFunction();
    }

    public Absolute(Function base) {
        super(FunctionTypes.ABSOLUTE);
        this.base = base;

        updateExpression();
    }

    @NotFullyImplemented
    @Override
    public void generateRandomFunction() {
        base = new Function("x+1");

        updateExpression();
    }

    protected void updateExpression() {
        var pbase = base.getSymjaExpression();

        setExpressions(F.Abs(pbase));
    }
}
