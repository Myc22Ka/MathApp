package pl.myc22ka.mathapp.model.function.functions;

import lombok.Getter;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.exceptions.ServerError;
import pl.myc22ka.mathapp.model.function.FunctionType;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.utils.math.MathUtils;

import java.util.List;

@Getter
public class Linear extends Function {
    private IExpr coefficient;
    private IExpr constant;

    // Constructor to generate random Linear Function
    public Linear() {
        super(FunctionType.LINEAR);

        generateRandomFunction();
    }

    // Constructor to get Linear Function with correct coefficients
    public Linear(IExpr coefficient, IExpr constant) {
        super(FunctionType.LINEAR);

        this.coefficient = coefficient;
        this.constant = constant;

        updateExpression();
    }

    public Linear(String rawExpression) {
        super(FunctionType.LINEAR, MathUtils.detectFirstVariable(rawExpression), rawExpression);
    }

    protected final void updateExpression() {
        setExpressions(F.Plus(F.Times(coefficient, variable), constant).toString());
    }

    @Override
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        if (answers.size() != 1) {
            throw new ServerError("Linear function requires exactly one root.");
        }

        this.coefficient = F.ZZ(1); // need to be gathered trough generator...
        this.constant = coefficient.negate().multiply(answers.getFirst());
    }


}
