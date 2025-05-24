package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import pl.myc22ka.mathapp.model.Expression;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.List;

public class Function extends Expression {
    public Function(String function) {
        super(FunctionTypes.FUNCTION, function);
    }

    public Function(IExpr function) {
        super(FunctionTypes.FUNCTION, function);
    }

    public Function(FunctionTypes type) {
        super(type);
    }

    @NotFullyImplemented
    public void generateRandomFunction() {
        rawExpression = "Cos(x)*Sin(3*x)";

        // TO DO...
    }

    @NotFullyImplemented
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        // TO DO...
    }

    public final Function plus(Function other) {
        return new Function((F.Plus(symjaExpression, F.Parenthesis(other.symjaExpression))));
    }

    public final Function minus(Function other) {
        return new Function(F.Subtract(symjaExpression, F.Parenthesis(other.symjaExpression)));
    }

    public final Function times(Function other) {
        return new Function(F.Times(symjaExpression, other.symjaExpression));
    }

    public final Function divide(Function other) {
        return new Function(F.Times(symjaExpression, F.Power(other.symjaExpression, F.CN1)));
    }

    public final Function composition(Function other) {
        var rule = F.Rule(evaluator.eval(getVariable() + "_"), evaluator.eval("HoldForm[" + rawExpression + "]"));

        return new Function(other.symjaExpression.replaceAll(rule));
    }
}
