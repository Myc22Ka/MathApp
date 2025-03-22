package pl.myc22ka.mathapp.model.functions;

import pl.myc22ka.mathapp.model.Function;
import pl.myc22ka.mathapp.model.FunctionTypes;

import java.util.List;
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
    public void generateFunctionFromAnswers(List<Double> answers) {
        if (answers.size() != 1) {
            throw new IllegalArgumentException("Linear function requires exactly one root.");
        }

        this.coefficient = 1;          // need to be gathered trough generator...
        this.constant = -this.coefficient * answers.getFirst();
    }
}
