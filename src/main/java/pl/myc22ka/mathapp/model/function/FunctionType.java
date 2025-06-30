package pl.myc22ka.mathapp.model.function;

public enum FunctionType {
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

    public static FunctionType parse(String text) {
        if (text == null) return FUNCTION;

        try {
            return valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            return FUNCTION; // Default value
        }
    }
}
