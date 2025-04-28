package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.function.Function;

import java.util.List;

public class Trigonometric extends pl.myc22ka.mathapp.model.functions.Function {
    private IExpr coefficient;
    private IExpr angle;
    private Function<IExpr, IExpr> trigFunction;

    // Konstruktor dla losowej funkcji trygonometrycznej
    public Trigonometric(ISymbol variable) {
        super(FunctionTypes.TRIGONOMETRIC, variable);

        generateRandomFunction();
        updateExpression();
    }

    // Konstruktor z pełnymi danymi
    public Trigonometric(ISymbol variable, IExpr coefficient, IExpr angle, Function<IExpr, IExpr> trigFunction) {
        super(FunctionTypes.TRIGONOMETRIC, variable);
        this.coefficient = coefficient;
        this.angle = angle;
        this.trigFunction = trigFunction;

        updateExpression();
    }

    @Override
    protected void updateExpression() {
        if (coefficient == null || angle == null || trigFunction == null) {
            setExpression("0");
            return;
        }

        IExpr inner = F.Times(angle, variable);
        IExpr trigExpr = trigFunction.apply(inner);
        IExpr fullExpr = F.Times(coefficient, trigExpr);

        setExpression(fullExpr.toString());
    }

    @NotFullyImplemented
    @Override
    public void generateFunctionFromAnswers(List<IExpr> answers) {
        if (answers == null || answers.size() < 2) {
            throw new IllegalArgumentException("Potrzeba co najmniej dwóch miejsc zerowych.");
        }

        IExpr x1 = answers.get(0);
        IExpr x2 = answers.get(1);

        IExpr deltaX = F.Subtract(x2, x1);
        this.angle = F.Divide(F.Pi, deltaX);
        this.coefficient = F.ZZ(1);
        this.trigFunction = F::Sin; // domyślnie SIN

        updateExpression();
    }

    @NotFullyImplemented
    @Override
    public void generateRandomFunction() {
        // Można później dodać generator dla różnych współczynników i funkcji
        this.coefficient = F.ZZ(1); // domyślnie 1
        this.angle = F.ZZ(1); // domyślnie 1
        this.trigFunction = F::Cos; // np. domyślnie COS
    }

    public static Trigonometric sin(ISymbol variable, IExpr a, IExpr b) {
        return new Trigonometric(variable, a, b, F::Sin);
    }

    public static Trigonometric cos(ISymbol variable, IExpr a, IExpr b) {
        return new Trigonometric(variable, a, b, F::Cos);
    }

    public static Trigonometric tan(ISymbol variable, IExpr a, IExpr b) {
        return new Trigonometric(variable, a, b, F::Tan);
    }

    // itd. dla cot, sec, csc...
}
