package pl.myc22ka.mathapp.model.function.functions;

import org.matheclipse.core.expression.F;
import pl.myc22ka.mathapp.model.function.FunctionType;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.utils.math.MathUtils;

public class Absolute extends Function {
    private Function base;

    public Absolute() {
        super(FunctionType.ABSOLUTE);

        generateRandomFunction();
    }

    public Absolute(Function base) {
        super(FunctionType.ABSOLUTE);
        this.base = base;

        updateExpression();
    }

    public Absolute(String rawExpression) {
        super(FunctionType.ABSOLUTE, MathUtils.detectFirstVariable(rawExpression), rawExpression);
    }

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
