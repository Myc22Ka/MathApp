package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.model.Expression;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.List;

public class Function extends Expression {
    private IExpr function;

    public Function(FunctionTypes type, ISymbol variable) {
        super(type, variable);
    }

    public Function(ISymbol variable, IExpr function) {
        super(FunctionTypes.FUNCTION, variable);

        this.function = function;

        updateExpression();
    }

    @Override
    protected void updateExpression() {
        expression = F.eval(function);
    }

    @NotFullyImplemented
    public void generateRandomFunction() {

    }

    @Override
    @NotFullyImplemented
    public void generateFunctionFromAnswers(List<IExpr> answers) {
    }
}
