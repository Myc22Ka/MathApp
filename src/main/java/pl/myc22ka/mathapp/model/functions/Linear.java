package pl.myc22ka.mathapp.model.functions;

import lombok.Getter;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.exceptions.FunctionException;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.List;

@Getter
public class Linear extends Function {
    private IExpr coefficient;
    private IExpr constant;

    // Constructor to generate random Linear Function
    public Linear() {
        super(FunctionTypes.LINEAR);

        generateRandomFunction();
    }

    // Constructor to get Linear Function with correct coefficients
    public Linear(IExpr coefficient, IExpr constant) {
        super(FunctionTypes.LINEAR);

        this.coefficient = coefficient;
        this.constant = constant;

        updateExpression();
    }

    protected final void updateExpression() {
        setExpressions(F.Plus(F.Times(coefficient, variable), constant).toString());
    }

    @Override
    @NotFullyImplemented
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        if (answers.size() != 1) {
            throw new FunctionException("Linear function requires exactly one root.");
        }

        this.coefficient = F.ZZ(1); // need to be gathered trough generator...
        this.constant = coefficient.negate().multiply(answers.getFirst());
    }
}
