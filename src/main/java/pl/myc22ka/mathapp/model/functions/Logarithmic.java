package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.FunctionTypes;

public class Logarithmic extends Function {
    private IExpr base;
    private IExpr coefficient;

    public Logarithmic() {
        super(FunctionTypes.LOGARITHMIC);

        generateRandomFunction();
    }

    public Logarithmic(IExpr base, IExpr coefficient) {
        super(FunctionTypes.LOGARITHMIC);
        this.base = base;
        this.coefficient = coefficient;

        updateExpression();
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
