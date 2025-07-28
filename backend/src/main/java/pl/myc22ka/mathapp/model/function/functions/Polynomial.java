package pl.myc22ka.mathapp.model.function.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import pl.myc22ka.mathapp.model.function.FunctionType;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.utils.math.MathUtils;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.*;

public class Polynomial extends Function {
    private List<IExpr> coefficients;

    public Polynomial() {
        super(FunctionType.POLYNOMIAL);

        generateRandomFunction();
        updateExpression();
    }

    public Polynomial(List<IExpr> coefficients) {
        super(FunctionType.POLYNOMIAL);
        this.coefficients = new ArrayList<>(coefficients.reversed());

        updateExpression();
    }

    public Polynomial(String rawExpression) {
        super(FunctionType.POLYNOMIAL, MathUtils.detectFirstVariable(rawExpression), rawExpression);
    }

    protected void updateExpression() {
        IExpr expr = coefficients.getFirst();

        for (int i = 1; i < coefficients.size(); i++) {
            IExpr coefficient = coefficients.get(i);

            IExpr term = F.Times(coefficient, F.Power(variable, F.ZZ(i)));
            expr = expr.isZero() ? term : F.Plus(expr, term);
        }

        setExpressions(expr.toString());
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
