package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.interfaces.IRational;
import pl.myc22ka.mathapp.model.Function;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.model.Point;

import java.util.List;
import java.util.Locale;

public class Quadratic {
    private IRational coefficientA;
    private IRational coefficientB;
    private IRational constant;

//    // Random Quadratic
//    public Quadratic(char variable) {
//        super(FunctionTypes.QUADRATIC, variable);
//
//        generateRandomFunction();
//    }
//
//    // Quadratic from a, b and c
//    public Quadratic(IRational coefficientA, IRational coefficientB, IRational constant, char variable) {
//        super(FunctionTypes.QUADRATIC, variable);
//
//        this.coefficientA = coefficientA;
//        this.coefficientB = coefficientB;
//        this.constant = constant;
//    }
//
//    // Quadratic from derivative
//    public Quadratic(double derivativeA, double derivativeB, char variable) {
//        super(FunctionTypes.QUADRATIC, variable);
//
//        this.coefficientA = derivativeA / 2;
//        this.coefficientB = derivativeB;
//        this.constant = 0;              // need to be gathered trough generator...
//    }
//
//    // Quadratic from answers
//    public Quadratic(List<Double> answers, char variable){
//        super(FunctionTypes.QUADRATIC, variable);
//
//        generateFunctionFromAnswers(answers);
//    }
//
//    // Quadratic from vertex
//    public Quadratic(Point vertex, char variable){
//        super(FunctionTypes.QUADRATIC, variable);
//
//        this.coefficientA = 1;          // need to be gathered trough generator...
//        this.coefficientB = -2 * coefficientA * vertex.getX();
//        this.constant =  coefficientA * vertex.getX() * vertex.getX() + vertex.getY();
//    }
//
//    public Quadratic(Point vertex, double coefficientA, char variable) {
//        super(FunctionTypes.QUADRATIC, variable);
//
//        this.coefficientA = coefficientA;
//        this.coefficientB = -2 * coefficientA * vertex.getX();
//        this.constant = coefficientA * vertex.getX() * vertex.getX() + vertex.getY();
//    }
//
//    @Override
//    public void generateRandomFunction() {
//        coefficientA = 1;               // need to be gathered trough generator...
//        coefficientB = -1;              // need to be gathered trough generator...
//        constant = -6;                  // need to be gathered trough generator...
//
//        // not implemented yet...
//    }
//
//    // Standard form: ax^2 + bx + c
//    @Override
//    public String getFunctionString() {
//        StringBuilder sb = new StringBuilder();
//
//        if (coefficientA != 0) {
//            sb.append(String.format(Locale.US, "%.2f*%s^2", coefficientA, variable));
//        }
//
//        if (coefficientB != 0) {
//            if (coefficientB > 0) {
//                sb.append("+").append(String.format(Locale.US, "%.2f*%s", coefficientB, variable));
//            } else {
//                sb.append("-").append(String.format(Locale.US, "%.2f*%s", -coefficientB, variable));
//            }
//        }
//
//        if (constant != 0) {
//            if (constant > 0) {
//                sb.append("+").append(String.format(Locale.US, "%.2f", constant));
//            } else {
//                sb.append("-").append(String.format(Locale.US, "%.2f", -constant));
//            }
//        }
//
//        return sb.toString();
//    }
//
//    // Canonical form: a(x - p)^2 + q
//    public String getCanonicalForm() {
//        if (coefficientA == 0) return "Nie jest funkcją kwadratową";
//
//        double p = -coefficientB / (2 * coefficientA);
//        double q = constant - (coefficientB * coefficientB) / (4 * coefficientA);
//
//        if (Double.isNaN(p) || Double.isNaN(q) || Double.isInfinite(p) || Double.isInfinite(q)) return "Błąd obliczeń";
//
//        return String.format(Locale.US, "%.2f*(%s %s %.2f)^2 %s %.2f",
//                coefficientA, variable, (p < 0 ? "+" : "-"), Math.abs(p),
//                (q < 0 ? "-" : "+"), Math.abs(q));
//    }
//
//    // Factored form: a(x - x1)(x - x2) if real roots exist
//    public String getFactoredForm() {
//        if (coefficientA == 0) return "Nie jest funkcją kwadratową";
//
//        double discriminant = coefficientB * coefficientB - 4 * coefficientA * constant;
//
//        if (discriminant < 0) return "Brak rzeczywistych pierwiastków";
//
//        double x1 = (-coefficientB + Math.sqrt(discriminant)) / (2 * coefficientA);
//        double x2 = (-coefficientB - Math.sqrt(discriminant)) / (2 * coefficientA);
//
//        if (Double.isNaN(x1) || Double.isNaN(x2) || Double.isInfinite(x1) || Double.isInfinite(x2)) return "Błąd obliczeń";
//
//        return String.format(Locale.US, "%.2f*(%s %s %.2f)(%s %s %.2f)",
//                coefficientA, variable, (x1 < 0 ? "+" : "-"), Math.abs(x1),
//                variable, (x2 < 0 ? "+" : "-"), Math.abs(x2));
//    }
//
//    @Override
//    public void generateFunctionFromAnswers(List<Double> answers) {
//        if (answers.size() != 2) {
//            throw new IllegalArgumentException("Quadratic function requires exactly two roots.");
//        }
//
//        double r1 = answers.getFirst();
//        double r2 = answers.get(1);
//
//        // Expand (x - r1)(x - r2) = x^2 - (r1 + r2)x + (r1 * r2)
//        this.coefficientA = 1;          // need to be gathered trough generator...
//        this.coefficientB = -(r1 + r2);
//        this.constant = r1 * r2;
//    }
}
