package pl.myc22ka.mathapp.model.function;

import java.util.HashSet;
import java.util.Set;

public class FunctionTypeDetector {
    public static FunctionTypes detect(String expression) {
        String expr = expression.toLowerCase().trim();

        // Remove whitespace for easier pattern matching
        String cleanExpr = expr.replaceAll("\\s+", "");

        // Collect all detected function types
        Set<FunctionTypes> detectedTypes = new HashSet<>();

        // Check for each function type
        checkForConstant(cleanExpr, detectedTypes);
        checkForLinear(cleanExpr, detectedTypes);
        checkForQuadratic(cleanExpr, detectedTypes);
        checkForPolynomial(cleanExpr, detectedTypes);
        checkForTrigonometric(cleanExpr, detectedTypes);
        checkForLogarithmic(cleanExpr, detectedTypes);
        checkForSquareRoot(cleanExpr, detectedTypes);
        checkForExponential(cleanExpr, detectedTypes);
        checkForAbsolute(cleanExpr, detectedTypes);
        checkForRational(cleanExpr, detectedTypes);

        // If multiple types detected, return FUNCTION
        if (detectedTypes.size() > 1) {
            return FunctionTypes.FUNCTION;
        }

        // If exactly one type detected, return it
        if (detectedTypes.size() == 1) {
            return detectedTypes.iterator().next();
        }

        // Default fallback
        return FunctionTypes.FUNCTION;
    }

    private static void checkForConstant(String expr, Set<FunctionTypes> types) {
        // Constant if no variable present
        if (!containsVariable(expr)) {
            types.add(FunctionTypes.CONSTANT);
        }
    }

    private static void checkForLinear(String expr, Set<FunctionTypes> types) {
        // Linear: contains x but no higher powers, roots, trig, log, exp functions
        if (containsVariable(expr) &&
                !containsHigherPowers(expr) &&
                !containsTrigonometric(expr) &&
                !containsLogarithmic(expr) &&
                !containsSquareRoot(expr) &&
                !containsExponential(expr) &&
                !containsAbsolute(expr) &&
                !containsRational(expr)) {
            types.add(FunctionTypes.LINEAR);
        }
    }

    private static void checkForQuadratic(String expr, Set<FunctionTypes> types) {
        // Quadratic: contains x^2 or x*x but no higher powers
        if ((expr.contains("x^2") || expr.contains("x*x") || expr.matches(".*x\\s*\\*\\s*x.*")) &&
                !containsCubicOrHigher(expr) &&
                !containsTrigonometric(expr) &&
                !containsLogarithmic(expr) &&
                !containsSquareRoot(expr) &&
                !containsExponential(expr) &&
                !containsAbsolute(expr) &&
                !containsRational(expr)) {
            types.add(FunctionTypes.QUADRATIC);
        }
    }

    private static void checkForPolynomial(String expr, Set<FunctionTypes> types) {
        // Polynomial: contains x^n where n > 2
        if (containsCubicOrHigher(expr) &&
                !containsTrigonometric(expr) &&
                !containsLogarithmic(expr) &&
                !containsSquareRoot(expr) &&
                !containsExponential(expr) &&
                !containsAbsolute(expr) &&
                !containsRational(expr)) {
            types.add(FunctionTypes.POLYNOMIAL);
        }
    }

    private static void checkForTrigonometric(String expr, Set<FunctionTypes> types) {
        if (containsTrigonometric(expr)) {
            types.add(FunctionTypes.TRIGONOMETRIC);
        }
    }

    private static void checkForLogarithmic(String expr, Set<FunctionTypes> types) {
        if (containsLogarithmic(expr)) {
            types.add(FunctionTypes.LOGARITHMIC);
        }
    }

    private static void checkForSquareRoot(String expr, Set<FunctionTypes> types) {
        if (containsSquareRoot(expr)) {
            types.add(FunctionTypes.SQUAREROOT);
        }
    }

    private static void checkForExponential(String expr, Set<FunctionTypes> types) {
        if (containsExponential(expr)) {
            types.add(FunctionTypes.EXPONENTIAL);
        }
    }

    private static void checkForAbsolute(String expr, Set<FunctionTypes> types) {
        if (containsAbsolute(expr)) {
            types.add(FunctionTypes.ABSOLUTE);
        }
    }

    private static void checkForRational(String expr, Set<FunctionTypes> types) {
        if (containsRational(expr)) {
            types.add(FunctionTypes.RATIONAL);
        }
    }

    // Helper methods for detecting specific patterns
    private static boolean containsVariable(String expr) {
        return expr.matches(".*[a-z].*");
    }

    private static boolean containsHigherPowers(String expr) {
        // Check for x^n where n > 1, or repeated multiplication
        return expr.matches(".*[a-z]\\^[2-9].*") ||
                expr.matches(".*[a-z]\\^\\d{2,}.*") ||
                expr.contains("x^2") ||
                expr.contains("x*x") ||
                expr.matches(".*x\\s*\\*\\s*x.*");
    }

    private static boolean containsCubicOrHigher(String expr) {
        // Check for x^n where n >= 3
        return expr.matches(".*[a-z]\\^[3-9].*") ||
                expr.matches(".*[a-z]\\^\\d{2,}.*") ||
                expr.matches(".*x\\s*\\*\\s*x\\s*\\*\\s*x.*"); // x*x*x pattern
    }

    private static boolean containsTrigonometric(String expr) {
        return expr.contains("sin") ||
                expr.contains("cos") ||
                expr.contains("tan") ||
                expr.contains("sec") ||
                expr.contains("csc") ||
                expr.contains("cot") ||
                expr.contains("asin") ||
                expr.contains("acos") ||
                expr.contains("atan") ||
                expr.contains("sinh") ||
                expr.contains("cosh") ||
                expr.contains("tanh");
    }

    private static boolean containsLogarithmic(String expr) {
        return expr.contains("log") ||
                expr.contains("ln") ||
                expr.contains("lg");
    }

    private static boolean containsSquareRoot(String expr) {
        return expr.contains("sqrt") ||
                expr.contains("√") ||
                expr.matches(".*\\^\\(1/2\\).*") ||
                expr.matches(".*\\^0\\.5.*");
    }

    private static boolean containsExponential(String expr) {
        // Variable in exponent or e^something
        return expr.matches(".*\\^[a-z].*") ||
                expr.matches(".*e\\^.*") ||
                expr.matches(".*exp\\(.*") ||
                expr.matches(".*\\d+\\^[a-z].*");
    }

    private static boolean containsAbsolute(String expr) {
        return expr.contains("abs") ||
                expr.contains("|") ||
                expr.matches(".*\\|.*\\|.*");
    }

    private static boolean containsRational(String expr) {
        // Check for division with variables
        if (!expr.contains("/")) {
            return false;
        }

        // Simple heuristic: if there's division and variables, likely rational
        // This could be improved with more sophisticated parsing
        String[] parts = expr.split("/");
        if (parts.length > 1) {
            for (int i = 1; i < parts.length; i++) {
                if (containsVariable(parts[i])) {
                    return true;
                }
            }
        }
        return false;
    }
}
