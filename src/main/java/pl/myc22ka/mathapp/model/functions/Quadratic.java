package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import pl.myc22ka.mathapp.model.Function;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.model.Point;

import java.util.List;

public class Quadratic extends Function {
    private IExpr coefficientA;
    private IExpr coefficientB;
    private IExpr constant;

    // Random Quadratic
    public Quadratic(ISymbol variable) {
        super(FunctionTypes.QUADRATIC, variable);

        generateRandomFunction();
        updateExpression();
    }

    // Quadratic from a, b and c
    public Quadratic(IExpr coefficientA, IExpr coefficientB, IExpr constant, ISymbol variable) {
        super(FunctionTypes.QUADRATIC, variable);

        this.coefficientA = coefficientA;
        this.coefficientB = coefficientB;
        this.constant = constant;

        updateExpression();
    }

    // Quadratic from derivative
    public Quadratic(IExpr derivativeA, IExpr derivativeB, ISymbol variable) {
        super(FunctionTypes.QUADRATIC, variable);

        this.coefficientA = F.Divide(derivativeA, 2);
        this.coefficientB = derivativeB;
        this.constant = F.C0; // need to be gathered trough generator...

        updateExpression();
    }

    // Quadratic from answers
    public Quadratic(List<IExpr> answers, ISymbol variable) {
        super(FunctionTypes.QUADRATIC, variable);

        generateFunctionFromAnswers(answers);
        updateExpression();
    }

    // Quadratic from vertex
    public Quadratic(IExpr vertex, ISymbol variable) {
        super(FunctionTypes.QUADRATIC, variable);

        // Assuming vertex is a Symja point with getX() and getY() methods returning
        // IExpr
        this.coefficientA = F.C1; // Default coefficient of 1

        // Calculate B coefficient: -2 * a * x
        this.coefficientB = F.Times(F.CN2, this.coefficientA, vertex.getAt(0));

        // Calculate constant term: a * x^2 + y
        this.constant = F.Plus(
                F.Times(this.coefficientA, F.Power(vertex.getAt(0), F.C2)),
                vertex.getAt(1));

        updateExpression();
    }

    public Quadratic(Point vertex, IExpr coefficientA, ISymbol variable) {
        super(FunctionTypes.QUADRATIC, variable);

        this.coefficientA = coefficientA;
        this.coefficientB = F.Times(F.ZZ(-2), coefficientA, vertex.getX());
        this.constant = F.Plus(F.Times(coefficientA, vertex.getX(), vertex.getX()), vertex.getY());

        updateExpression();
    }

    // Create the expression: a * x^2 + b * x + c
    private void updateExpression() {
        IExpr expr = F.Plus(
                F.Times(coefficientA, F.Power(variable, F.C2)),
                F.Times(coefficientB, variable),
                constant);

        setExpression(expr.eval());
    }

    @Override
    public void generateRandomFunction() {
        coefficientA = F.ZZ(1); // need to be gathered trough generator...
        coefficientB = F.ZZ(-1); // need to be gathered trough generator...
        constant = F.ZZ(-6); // need to be gathered trough generator...

        // not implemented yet...
    }

    @Override
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        if (answers.size() != 2) {
            throw new IllegalArgumentException("Quadratic function requires exactly two roots.");
        }

        IExpr r1 = answers.getFirst();
        IExpr r2 = answers.get(1);

        // Expand (x - r1)(x - r2) = x^2 - (r1 + r2)x + (r1 * r2)
        this.coefficientA = F.ZZ(1); // need to be gathered trough generator...
        this.coefficientB = F.Negate(F.Plus(r1, r2));
        this.constant = F.Times(r1, r2);
    }
}
