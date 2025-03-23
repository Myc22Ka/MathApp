package pl.myc22ka.mathapp.model.functions;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.Function;
import pl.myc22ka.mathapp.model.FunctionTypes;
import pl.myc22ka.mathapp.utils.MathUtils;

import java.util.*;

public class Polynomial {
    private List<Double> coefficients;

//    public Polynomial(char variable) {
//        super(FunctionTypes.POLYNOMIAL, variable);
//
//        generateRandomFunction();
//    }
//
//    public Polynomial(List<Double> coefficients, char variable) {
//        super(FunctionTypes.POLYNOMIAL, variable);
//        this.coefficients = new ArrayList<>(coefficients);
//    }
//
//    @Override
//    public void generateRandomFunction() {
//        coefficients = List.of(1.0, 3.0, 1.0, 3.0);          // need to be gathered trough generator...
//
//        // not implemented yet...
//    }
//
//    @Override
//    public String getFunctionString() {
//        StringJoiner joiner = new StringJoiner("");
//        int degree = coefficients.size() - 1;
//
//        for (int i = 0; i < coefficients.size(); i++) {
//            double coef = coefficients.get(i);
//            int power = degree - i;
//
//            if (coef == 0) continue;
//
//            String sign = (coef < 0) ? "-" : (joiner.length() > 0 ? "+" : "");
//            double absCoef = Math.abs(coef);
//
//            String coefStr = (absCoef != 1 || power == 0) ? String.format(Locale.US, "%.2f", absCoef) : "";
//
//            String powerStr = (power > 0) ? (power == 1 ? String.valueOf(variable) : variable + "^" + power) : "";
//
//            joiner.add(sign + coefStr + powerStr);
//        }
//
//        return joiner.toString();
//    }
//
//    public String getCanonicalForm() {
//        var roots = getAllRoots();
//        double gcd = MathUtils.getPolynomialGCD(coefficients);
//
//        // Jeśli GCD jest większe niż 1, uwzględnij go w prefiksie
//        String prefix = (gcd != 1) ? String.format(Locale.US, "%.2f*", gcd) : "";
//        StringBuilder result = new StringBuilder(prefix);
//
//        // Pobierz pierwiastki rzeczywiste
//        var realRoots = getRealRoots();
//
//        for (String root : realRoots) {
//            double rootValue = Double.parseDouble(root);
//
//            if (Math.abs(rootValue) < 1e-10) {
//                result.append("x");
//            } else {
//                result.append(String.format("(x%s%.2f)", (rootValue < 0 ? "+" : "-"), Math.abs(rootValue)));
//            }
//        }
//
//        ExprEvaluator util = new ExprEvaluator(); // Unikamy konwersji na zmiennoprzecinkowe
//        util.eval("SetOptions(\"RationalConstants\", True)");
//
//        System.out.println(result.toString());
//
//        // Konwersja StringBuilder na String
//        String divisor = result.toString();
//
//        // Poprawne wywołanie PolynomialQuotientRemainder
//        String command = String.format("PolynomialRemainder(" + getFunctionString()+ "," +divisor +",x)");
//        IExpr r = util.eval(command);
//
//        // Wyświetlenie wyniku
//        System.out.println(r);
//
//        return r.toString();
//    }
//
//
//    @Override
//    public void generateFunctionFromAnswers(List<Double> answers) {
//        if (answers == null || answers.isEmpty()) {
//            throw new IllegalArgumentException("Musisz podać przynajmniej jedno miejsce zerowe.");
//        }
//
//        double a = 1.0;     // need to be gathered trough generator...
//
//        List<Double> result = new ArrayList<>(Collections.nCopies(answers.size() + 1, 0.0));
//        result.set(0, a);
//
//        for (double root : answers) {
//            for (int i = result.size() - 1; i > 0; i--) {
//                result.set(i, result.get(i) - root * result.get(i - 1));
//            }
//        }
//
//        this.coefficients = result;
//    }
}
