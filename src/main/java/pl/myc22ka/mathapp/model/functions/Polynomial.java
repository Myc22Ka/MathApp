package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.*;

public class Polynomial extends Function {
    private List<IExpr> coefficients;

    public Polynomial(ISymbol variable) {
        super(FunctionTypes.POLYNOMIAL, variable);

        generateRandomFunction();
        updateExpression();
    }

    public Polynomial(List<IExpr> coefficients, ISymbol variable) {
        super(FunctionTypes.POLYNOMIAL, variable);
        this.coefficients = new ArrayList<>(coefficients.reversed());

        updateExpression();
    }

    /**
     * Updates the expression field in the parent class based on the coefficients.
     */
    @Override
    protected void updateExpression() {
        if (coefficients == null || coefficients.isEmpty()) {
            setExpression("0");
            return;
        }

        IExpr expr = coefficients.getFirst();
        for (int i = 1; i < coefficients.size(); i++) {
            IExpr coefficient = coefficients.get(i);

            IExpr term = F.Times(coefficient, F.Power(variable, F.ZZ(i)));
            expr = expr.isZero() ? term : F.Plus(expr, term);
        }

        setExpression(expr.toString());
    }

    @Override
    @NotFullyImplemented
    public void generateRandomFunction() {
        coefficients = List.of(F.ZZ(1), F.ZZ(3), F.ZZ(1), F.ZZ(3)); // need to be gathered trough generator...

        // not implemented yet...
    }

    @Override
    @NotFullyImplemented
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException("Musisz podać przynajmniej jedno miejsce zerowe.");
        }

        IExpr a = F.ZZ(1); // need to be gathered trough generator...

        List<IExpr> result = new ArrayList<>(Collections.nCopies(answers.size() + 1, F.C0));
        result.set(0, a);

        for (IExpr root : answers) {
            for (int i = result.size() - 1; i > 0; i--) {
                IExpr term = F.Times(root, result.get(i - 1));
                result.set(i, F.Subtract(result.get(i), term));
            }
        }

        this.coefficients = result;
    }
}
