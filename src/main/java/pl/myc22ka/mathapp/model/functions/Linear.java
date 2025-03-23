package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.Function;
import pl.myc22ka.mathapp.model.FunctionTypes;

import java.util.List;
import java.util.Locale;

public class Linear extends Function {
    private IExpr coefficient;
    private IExpr constant;

    // Constructor to generate random Linear Function
    public Linear(char variable) {
        super(FunctionTypes.LINEAR, variable);

        generateRandomFunction();
    }

    // Constructor to get Linear Function with correct coefficients
    public Linear(IExpr coefficient, IExpr  constant, char variable){
        super(FunctionTypes.LINEAR, variable);

        this.coefficient = coefficient;
        this.constant = constant;
    }

    @Override
    public void generateRandomFunction() {
        coefficient = F.ZZ(1);       // need to be gathered trough generator...
        constant = F.ZZ(1);          // need to be gathered trough generator...

        // not implemented yet ...
    }

    @Override
    public String getFunctionString() {
        return String.format(Locale.US, "%s%c%s%s",
                coefficient.toString(),
                variable,
                (constant.isNegative() ? "" : "+"),
                constant.toString()
        );
    }

    @Override
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        if (answers.size() != 1) {
            throw new IllegalArgumentException("Linear function requires exactly one root.");
        }

        this.coefficient = F.ZZ(1);                 // need to be gathered trough generator...
        this.constant = coefficient.negate().multiply(answers.getFirst());
    }
}
