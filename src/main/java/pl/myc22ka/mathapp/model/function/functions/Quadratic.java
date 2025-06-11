package pl.myc22ka.mathapp.model.function.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import pl.myc22ka.mathapp.model.function.FunctionTypes;
import pl.myc22ka.mathapp.model.function.Function;
import pl.myc22ka.mathapp.utils.math.MathUtils;
import pl.myc22ka.mathapp.utils.functions.Point;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.List;

public class Quadratic extends Function {
    private IExpr coefficientA;
    private IExpr coefficientB;
    private IExpr constant;

    // Random Quadratic
    public Quadratic() {
        super(FunctionTypes.QUADRATIC);

        generateRandomFunction();
        updateExpression();
    }

    // Quadratic from a, b and c
    public Quadratic(IExpr coefficientA, IExpr coefficientB, IExpr constant) {
        super(FunctionTypes.QUADRATIC);

        this.coefficientA = coefficientA;
        this.coefficientB = coefficientB;
        this.constant = constant;

        updateExpression();
    }

    // Quadratic from derivative
    public Quadratic(Linear derivative) {
        super(FunctionTypes.QUADRATIC);

        this.coefficientA = F.Divide(derivative.getCoefficient(), 2);
        this.coefficientB = derivative.getConstant();
        this.constant = F.C0; // need to be gathered trough generator...

        updateExpression();
    }

    // Quadratic from answers
    public Quadratic(List<IExpr> answers) {
        super(FunctionTypes.QUADRATIC);

        generateFunctionFromAnswers(answers);
        updateExpression();
    }

    // Quadratic from vertex
    public Quadratic(Point vertex) {
        super(FunctionTypes.QUADRATIC);

        this.coefficientA = F.C1; // need to be gathered trough generator...
        this.coefficientB = F.Times(F.CN2, this.coefficientA, vertex.getX());
        this.constant = F.Plus(F.Times(this.coefficientA, F.Power(vertex.getX(), F.C2)), vertex.getY());

        updateExpression();
    }

    public Quadratic(Point vertex, IExpr coefficientA, ISymbol variable) {
        super(FunctionTypes.QUADRATIC);

        this.coefficientA = coefficientA;
        this.coefficientB = F.Times(F.ZZ(-2), coefficientA, vertex.getX());
        this.constant = F.Plus(F.Times(coefficientA, vertex.getX(), vertex.getX()), vertex.getY());

        updateExpression();
    }

    public Quadratic(String rawExpression) {
        super(FunctionTypes.QUADRATIC, MathUtils.detectFirstVariable(rawExpression), rawExpression);
    }

    protected void updateExpression() {
        setExpressions(F.Plus(F.Times(coefficientA, F.Power(variable, F.C2)), F.Times(coefficientB, variable), constant).toString());
    }

    @Override
    @NotFullyImplemented
    public void generateRandomFunction() {
        coefficientA = F.ZZ(1); // need to be gathered trough generator...
        coefficientB = F.ZZ(-1); // need to be gathered trough generator...
        constant = F.ZZ(-6); // need to be gathered trough generator...

        // not implemented yet...
    }

    @Override
    @NotFullyImplemented
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        if (answers.size() != 2) {
            throw new IllegalArgumentException("Quadratic function requires exactly two roots.");
        }

        IExpr r1 = answers.getFirst();
        IExpr r2 = answers.get(1);

        this.coefficientA = F.ZZ(1); // need to be gathered trough generator...
        this.coefficientB = F.Negate(F.Plus(r1, r2));
        this.constant = F.Times(r1, r2);
    }
}
