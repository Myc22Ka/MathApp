package pl.myc22ka.mathapp.model.function.functions;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.function.FunctionTypes;
import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.util.function.Function;

import java.util.List;

public class Trigonometric extends pl.myc22ka.mathapp.model.function.Function {
    private IExpr coefficient;
    private IExpr angle;
    private Function<IExpr, IExpr> trigFunction;

    public Trigonometric() {
        super(FunctionTypes.TRIGONOMETRIC);

        generateRandomFunction();
        updateExpression();
    }

    // Konstruktor z pełnymi danymi
    public Trigonometric(IExpr coefficient, IExpr angle, Function<IExpr, IExpr> trigFunction) {
        super(FunctionTypes.TRIGONOMETRIC);
        this.coefficient = coefficient;
        this.angle = angle;
        this.trigFunction = trigFunction;

        updateExpression();
    }

    protected void updateExpression() {
        IExpr inner = F.Times(angle, variable);
        IExpr trigExpr = trigFunction.apply(inner);
        IExpr fullExpr = F.Times(coefficient, trigExpr);

        setExpressions(fullExpr.toString());
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

    public static Trigonometric sin(IExpr a, IExpr b) {
        return new Trigonometric(a, b, F::Sin);
    }

    public static Trigonometric cos(IExpr a, IExpr b) {
        return new Trigonometric(a, b, F::Cos);
    }

    public static Trigonometric tan(IExpr a, IExpr b) {
        return new Trigonometric(a, b, F::Tan);
    }

    // itd. dla cot, sec, csc...
}
