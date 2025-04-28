package pl.myc22ka.mathapp.model.functions;

import lombok.Getter;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.List;

@Getter
public class Linear extends Function {
    private IExpr coefficient;
    private IExpr constant;

    // Constructor to generate random Linear Function
    public Linear(ISymbol variable) {
        super(FunctionTypes.LINEAR, variable);

        generateRandomFunction();
    }

    // Constructor to get Linear Function with correct coefficients
    public Linear(IExpr coefficient, IExpr constant, ISymbol variable) {
        super(FunctionTypes.LINEAR, variable);

        this.coefficient = coefficient;
        this.constant = constant;

        updateExpression();
    }

    @Override
    @NotFullyImplemented
    public void generateRandomFunction() {
        coefficient = F.ZZ(1); // need to be gathered trough generator...
        constant = F.ZZ(1); // need to be gathered trough generator...

        updateExpression();

        // not implemented yet ...
    }

    /**
     * Updates the expression field in the parent class based on coefficient and
     * constant
     */
    @Override
    protected void updateExpression() {
        setExpression(F.Plus(F.Times(coefficient, variable), constant).toString());
    }

    @Override
    @NotFullyImplemented
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        if (answers.size() != 1) {
            throw new IllegalArgumentException("Linear function requires exactly one root.");
        }

        this.coefficient = F.ZZ(1); // need to be gathered trough generator...
        this.constant = coefficient.negate().multiply(answers.getFirst());
    }
}
