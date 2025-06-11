package pl.myc22ka.mathapp.model.function;

public enum FunctionTypes {
    CONSTANT,
    LINEAR,
    QUADRATIC,
    POLYNOMIAL,
    TRIGONOMETRIC,
    SQUAREROOT,
    EXPONENTIAL,
    RATIONAL,
    LOGARITHMIC,
    ABSOLUTE,
    FUNCTION;

    public static FunctionTypes parse(String text) {
        if (text == null) return FUNCTION;

        try {
            return valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            return FUNCTION; // Default value
        }
    }
}
