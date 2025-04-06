package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.model.Expression;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.List;

public class Function extends Expression {
    public Function(FunctionTypes type, ISymbol variable) {
        super(type, variable);

        generateRandomFunction();
    }

    public Function(String function, ISymbol variable) {
        super(FunctionTypes.FUNCTION, variable, F.eval(function));

        setExpression(F.eval(function));
    }

    protected void updateExpression() {}

    @NotFullyImplemented
    public void generateRandomFunction() {
        setExpression(F.eval("Cos(x)*Sin(3*x)"));

        // TO DO...
    }

    @NotFullyImplemented
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        // TO DO...
    }

    public final Function plus(Function other) {
        return new Function( F.Plus(this.getExpression(), other.getExpression()).toString(), this.getVariable());
    }
}
