package pl.myc22ka.mathapp.model.function.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import pl.myc22ka.mathapp.exceptions.ServerErrorMessages;
import pl.myc22ka.mathapp.exceptions.ServerError;
import pl.myc22ka.mathapp.model.function.FunctionType;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.utils.math.MathUtils;

import java.util.List;

public class Constant extends Function {
    private IExpr value;

    public Constant() {
        super(FunctionType.CONSTANT);

        generateRandomFunction();
    }

    public Constant(IExpr value) {
        super(FunctionType.CONSTANT);

        if (value == null) {
            throw new ServerError(ServerErrorMessages.NULL_VALUE_NOT_ALLOWED);
        }

        this.value = value;

        updateExpression();
    }

    public Constant(String rawExpression) {
        super(FunctionType.CONSTANT, MathUtils.detectFirstVariable(rawExpression), rawExpression);
    }

    @Override
    public void generateRandomFunction() {
        value = F.ZZ(1);

        updateExpression();
    }

    protected void updateExpression() {


        if (value == null) {
            throw new ServerError(ServerErrorMessages.NULL_VALUE_UPDATE_ERROR);
        }

        setExpressions(value);
    }

    @Override
    public IExpr getRange() {
        return F.Dummy("R");
    }

    @Override
    public List<IExpr> getRealRoots() {
        return rawExpression.equals("0") ? List.of(F.True) : List.of(F.False);
    }
}
