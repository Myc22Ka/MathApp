package pl.myc22ka.mathapp.model.function.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import pl.myc22ka.mathapp.exceptions.FunctionErrorMessages;
import pl.myc22ka.mathapp.exceptions.FunctionException;
import pl.myc22ka.mathapp.model.function.FunctionTypes;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.utils.math.MathSymbols;
import pl.myc22ka.mathapp.utils.math.MathUtils;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;
import pl.myc22ka.mathapp.utils.annotations.NotTested;

import java.util.ArrayList;
import java.util.List;

public class Constant extends Function {
    private IExpr value;

    public Constant() {
        super(FunctionTypes.CONSTANT);

        generateRandomFunction();
    }

    public Constant(IExpr value) {
        super(FunctionTypes.CONSTANT);

        if (value == null) {
            throw new FunctionException(FunctionErrorMessages.NULL_VALUE_NOT_ALLOWED);
        }

        this.value = value;

        updateExpression();
    }

    public Constant(String rawExpression) {
        super(FunctionTypes.CONSTANT, MathUtils.detectFirstVariable(rawExpression), rawExpression);
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
            throw new FunctionException(FunctionErrorMessages.NULL_VALUE_UPDATE_ERROR);
        }

        setExpressions(value);
    }

    @Override
    public IExpr getRange() {
        return MathSymbols.belongs(F.y, MathSymbols.getReal());
    }

    @Override
    public List<IExpr> getRealRoots() {
        return rawExpression.equals("0") ? List.of(F.True) : List.of(F.False);
    }
}
