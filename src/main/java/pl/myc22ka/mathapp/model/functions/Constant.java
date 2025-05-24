package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import pl.myc22ka.mathapp.exceptions.FunctionErrorMessages;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;
import pl.myc22ka.mathapp.utils.annotations.NotTested;

public class Constant extends Function {
    private IExpr value;

    public Constant() {
        super(FunctionTypes.CONSTANT);

        generateRandomFunction();
    }

    public Constant(IExpr value) {
        super(FunctionTypes.CONSTANT);

        if (value == null) {
            throw new IllegalArgumentException(FunctionErrorMessages.NULL_VALUE_NOT_ALLOWED.getMessage());
        }

        this.value = value;

        updateExpression();
    }

    @NotFullyImplemented
    @NotTested
    @Override
    public void generateRandomFunction() {
        value = F.ZZ(1);

        updateExpression();
    }

    protected void updateExpression() {

        if (value == null) {
            throw new IllegalStateException(FunctionErrorMessages.NULL_VALUE_UPDATE_ERROR.getMessage());
        }

        setExpressions(value);
    }
}
