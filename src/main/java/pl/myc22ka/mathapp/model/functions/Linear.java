package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.eval.ExprEvaluator;
import pl.myc22ka.mathapp.model.Function;
import pl.myc22ka.mathapp.model.FunctionTypes;

import java.util.Locale;
import java.util.Random;

public class Linear extends Function {
    private double coefficient;                             // Współczynnik
    private double constant;                                // Wyraz wolny
    private static final Random RANDOM = new Random();

    // Constructor to generate random Linear Function
    public Linear(char variable) {
        super(FunctionTypes.LINEAR, variable);

        generateRandomFunction();
    }

    // Constructor to get Linear Function with correct coefficients
    public Linear(double coefficient, double constant, char variable){
        super(FunctionTypes.LINEAR, variable);

        this.coefficient = coefficient;
        this.constant = constant;
    }

    public Linear(double answer, char variable){
        super(FunctionTypes.LINEAR, variable);

        generateFunctionFromAnswers(answer);
    }

    @Override
    public void generateRandomFunction() {
        coefficient = RANDOM.nextDouble() * 20 - 10;
        constant = RANDOM.nextDouble() * 20 - 10;

        if (coefficient == 0) {
            coefficient = RANDOM.nextDouble() * 10 + 1;
        }
    }

    @Override
    public String getFunctionString() {
        return String.format(Locale.US, "%.2f%c%s%.2f",
                coefficient,
                variable,
                (constant < 0 ? "" : "+"),
                constant
        );
    }

    @Override
    public void generateFunctionFromAnswers(double... answers) {
        do {
            this.coefficient = RANDOM.nextDouble() * 20 - 10; // Random value in range (-10,10)
        } while (this.coefficient == 0);

        this.constant = -this.coefficient * answers[0];
    }
}
