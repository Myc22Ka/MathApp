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
        super(FunctionTypes.FUNCTION, variable, function);
    }

    protected void updateExpression() {
    }

    @NotFullyImplemented
    public void generateRandomFunction() {
        setExpression("Cos(x)*Sin(3*x)");

        // TO DO...
    }

    @NotFullyImplemented
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        // TO DO...
    }

    public final Function plus(Function other) {
        return new Function((F.Plus(parseExpression(), F.Parenthesis(other.parseExpression()))).toString(), variable);
    }

    public final Function minus(Function other) {
        return new Function(F.Subtract(parseExpression(), F.Parenthesis(other.parseExpression())).toString(), variable);
    }

    public final Function times(Function other) {
        return new Function(F.Times(parseExpression(), other.parseExpression()).toString(), variable);
    }

    public final Function divide(Function other) {
        return new Function(F.Divide(parseExpression(), other.parseExpression()).toString(), variable);
    }

    public final Function composition(Function other) {
        var rule = F.Rule(evaluator.eval(variable + "_"), evaluator.eval("HoldForm[" + expression + "]"));

        IExpr result = other.parseExpression().replaceAll(rule);

        return new Function(result.toString(), variable);
    }
}
