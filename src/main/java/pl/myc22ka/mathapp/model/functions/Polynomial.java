package pl.myc22ka.mathapp.model.functions;

import pl.myc22ka.mathapp.model.Function;
import pl.myc22ka.mathapp.model.FunctionTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Polynomial extends Function {
    private List<Double> coefficients;

    public Polynomial(char variable) {
        super(FunctionTypes.POLYNOMIAL, variable);

        generateRandomFunction();
    }

    public Polynomial(List<Double> coefficients, char variable) {
        super(FunctionTypes.POLYNOMIAL, variable);
        this.coefficients = new ArrayList<>(coefficients);
    }

    @Override
    public void generateRandomFunction() {
        coefficients = List.of(1.0, 3.0, 1.0, 3.0);          // need to be gathered trough generator...

        // not implemented yet...
    }

    @Override
    public String getFunctionString() {
        StringBuilder sb = new StringBuilder();
        int degree = coefficients.size() - 1;

        for (int i = 0; i < coefficients.size(); i++) {
            double coef = coefficients.get(i);
            int power = degree - i;

            if (coef == 0) continue; // Pomijamy zerowe współczynniki

            // Formatowanie współczynnika
            if (!sb.isEmpty()) {
                sb.append(coef > 0 ? " + " : " - ");
            } else {
                if (coef < 0) sb.append("-");
            }

            // Wartość absolutna współczynnika (nie dodajemy `1` przy x^n)
            if (Math.abs(coef) != 1 || power == 0) {
                sb.append(String.format(Locale.US, "%.2f", Math.abs(coef)));
            }

            // Dodanie zmiennej i wykładnika
            if (power > 0) {
                sb.append(variable);
                if (power > 1) sb.append("^").append(power);
            }
        }

        return sb.toString();
    }
    @Override
    public void generateFunctionFromAnswers(List<Double> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException("Musisz podać przynajmniej jedno miejsce zerowe.");
        }

        // Startujemy od wielomianu 1 (współczynnik a = 1)
        List<Double> result = new ArrayList<>(Collections.nCopies(answers.size() + 1, 0.0));
        result.set(0, 1.0); // x^n ma współczynnik 1 need to be gathered trough generator...

        // Mnożymy kolejne czynniki (x - x_i)
        for (double root : answers) {
            for (int i = result.size() - 1; i > 0; i--) {
                result.set(i, result.get(i) - root * result.get(i - 1));
            }
        }

        this.coefficients = result;
    }
}
