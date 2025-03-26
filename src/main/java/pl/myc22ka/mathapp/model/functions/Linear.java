package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.model.Function;
import pl.myc22ka.mathapp.model.FunctionTypes;

import java.util.List;

public class Linear extends Function {
    private IExpr coefficient;
    private IExpr constant;

    // Constructor to generate random Linear Function
    public Linear(ISymbol variable) {
        super(FunctionTypes.LINEAR, variable);

        generateRandomFunction();
        updateExpression();
    }

    // Constructor to get Linear Function with correct coefficients
    public Linear(IExpr coefficient, IExpr constant, ISymbol variable) {
        super(FunctionTypes.LINEAR, variable);

        this.coefficient = coefficient;
        this.constant = constant;

        updateExpression();
    }

    @Override
    public void generateRandomFunction() {
        coefficient = F.ZZ(1); // need to be gathered trough generator...
        constant = F.ZZ(1); // need to be gathered trough generator...

        // not implemented yet ...
    }

    /**
     * Updates the expression field in the parent class based on coefficient and
     * constant
     */
    private void updateExpression() {
        // Create the expression: coefficient * variable + constant
        IExpr expr = F.Plus(
                F.Times(coefficient, variable),
                constant);

        // Set the parent class expression field
        setExpression(expr);
    }

    @Override
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        if (answers.size() != 1) {
            throw new IllegalArgumentException("Linear function requires exactly one root.");
        }

        this.coefficient = F.ZZ(1); // need to be gathered trough generator...
        this.constant = coefficient.negate().multiply(answers.getFirst());
    }
}
