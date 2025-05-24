package pl.myc22ka.mathapp.model.function.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.function.FunctionTypes;
import pl.myc22ka.mathapp.model.function.Function;

public class Rational extends Function {
    private IExpr numerator;
    private IExpr denominator;

    public Rational() {
        super(FunctionTypes.RATIONAL);
        generateRandomFunction();
    }

    public Rational(IExpr numerator, IExpr denominator) {
        super(FunctionTypes.RATIONAL);
        this.numerator = numerator;
        this.denominator = denominator;
        updateExpression();
    }

    @Override
    public void generateRandomFunction() {
        numerator = F.Plus(F.ZZ(2), F.Times(F.ZZ(3), variable));  // Random numerator
        denominator = F.Plus(F.ZZ(1), F.Times(F.ZZ(4), variable)); // Random denominator
        updateExpression();
    }

    protected void updateExpression() {
        setExpressions(F.Divide(numerator, denominator).toString());
    }
}
