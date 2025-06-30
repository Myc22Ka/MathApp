package pl.myc22ka.mathapp.model.function.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.function.FunctionType;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.utils.math.MathUtils;

public class Logarithmic extends Function {
    private IExpr base;
    private IExpr coefficient;

    public Logarithmic() {
        super(FunctionType.LOGARITHMIC);

        generateRandomFunction();
    }

    public Logarithmic(IExpr base, IExpr coefficient) {
        super(FunctionType.LOGARITHMIC);
        this.base = base;
        this.coefficient = coefficient;

        updateExpression();
    }

    public Logarithmic(String rawExpression) {
        super(FunctionType.LOGARITHMIC, MathUtils.detectFirstVariable(rawExpression), rawExpression);
    }

    @Override
    public void generateRandomFunction() {
        base = F.ZZ(2);  // Log base 2
        coefficient = F.ZZ(1); // Default coefficient
        updateExpression();
    }

    protected void updateExpression() {
        setExpressions(F.Times(coefficient, F.Log(base, getVariable())).toString());
    }
}
