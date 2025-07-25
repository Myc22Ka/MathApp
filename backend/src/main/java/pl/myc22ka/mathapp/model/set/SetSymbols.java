package pl.myc22ka.mathapp.model.set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum representing common set symbols with Symja and display forms.
 *
 * @author Myc22Ka
 * @version 1.0.4
 * @since 2025.06.19
 */
public enum SetSymbols {

    /**
     * Real set symbols.
     */
    REAL("Reals", "ℝ"),
    /**
     * Complex set symbols.
     */
    COMPLEX("Complexes", "ℂ"),
    /**
     * Integer set symbols.
     */
    INTEGER("Integers", "ℤ"),
    /**
     * Natural set symbols.
     */
    NATURAL("Naturals", "ℕ"),
    /**
     * Rational set symbols.
     */
    RATIONAL("Rationals", "ℚ"),
    /**
     * Empty set symbols.
     */
    EMPTY("EmptySet", "∅"),
    /**
     * Union set symbols.
     */
    UNION("Union", "∪"),
    /**
     * Intersection set symbols.
     */
    INTERSECTION("Intersection", "∩"),
    /**
     * Difference set symbols.
     */
    DIFFERENCE("Complement", "∖"),
    /**
     * Element of set symbols.
     */
    ELEMENT_OF("Element", "∈"),
    /**
     * Subset set symbols.
     */
    SUBSET("Subset", "⊆"),
    /**
     * Proper subset set symbols.
     */
    PROPER_SUBSET("ProperSubset", "⊂"),
    /**
     * Infinity set symbols.
     */
    INFINITY("Infinity", "∞"),
    /**
     * Negative infinity set symbols.
     */
    NEGATIVE_INFINITY("Infinity", "-∞"),
    /**
     * And set symbols.
     */
    AND("And", "∧"),
    /**
     * Or set symbols.
     */
    OR("Or", "∨"),
    /**
     * No operation set symbols.
     */
    NO_OPERATION("NoOperation", "NO");


    private final String symjaSymbol;
    private final String displaySymbol;
    private final ExprEvaluator evaluator = new ExprEvaluator();

    private static final Map<String, SetSymbols> DISPLAY_LOOKUP;

    static {
        Map<String, SetSymbols> displayMap = new HashMap<>();
        for (SetSymbols symbol : values()) {
            displayMap.put(symbol.displaySymbol, symbol);
        }
        DISPLAY_LOOKUP = Collections.unmodifiableMap(displayMap);
    }

    SetSymbols(String symjaSymbol, String displaySymbol) {
        this.symjaSymbol = symjaSymbol;
        this.displaySymbol = displaySymbol;
    }

    /**
     * Checks if the given element belongs to this set.
     *
     * @param element the element to check
     * @return true if element is in the set, false otherwise
     */
    public boolean contains(String element) {
        IExpr result;

        try {
            result = evaluator.eval(element);
        } catch (Exception e) {
            return false;
        }

        return switch (this) {
            case REAL -> {
                IExpr imagPart = evaluator.eval(F.Im(result));
                yield imagPart.isZero() ||
                        (imagPart.isNumericFunction() && imagPart.evalf() == 0);
            }
            case COMPLEX -> result.isComplex();
            case INTEGER -> result.isInteger();
            case NATURAL -> result.isInteger() && Integer.parseInt(result.toString()) >= 0;
            case RATIONAL -> result.isRational();
            default -> false;
        };
    }

    /**
     * Parses the Symja symbol into an expression.
     *
     * @return parsed Symja expression
     */
    public IExpr parse() {
        return evaluator.getEvalEngine().parse(symjaSymbol);
    }

    /**
     * Get binary operations list.
     *
     * @return the list
     */
    public static @NotNull @Unmodifiable List<SetSymbols> getBinaryOperations(){
        return List.of(
                SetSymbols.DIFFERENCE,
                SetSymbols.UNION,
                SetSymbols.INTERSECTION
        );
    }

    /**
     * Gets symbols.
     *
     * @return the symbols
     */
    public static @NotNull @Unmodifiable List<SetSymbols> getSymbols() {
        return List.of(
                SetSymbols.REAL,
                SetSymbols.INTEGER,
                SetSymbols.COMPLEX,
                SetSymbols.NATURAL,
                SetSymbols.RATIONAL,
                SetSymbols.EMPTY
        );
    }

    /**
     * Returns a list of logical set operation symbols.
     *
     * @return an unmodifiable list of logical set symbols
     */
    public static @NotNull @Unmodifiable List<SetSymbols> getSetLogicSymbols() {
        return List.of(
                SetSymbols.OR,
                SetSymbols.AND
        );
    }

    /**
     * Returns string of all binary operations like this: {@code ∖∪∩△}.
     *
     * @return binary operations in single string
     */
    public static @NotNull String getBinaryOperationsString() {
        return getBinaryOperations().stream()
                .map(SetSymbols::toString)
                .collect(Collectors.joining(""));
    }

    /**
     * Checks if the given element represents the real number set (R).
     *
     * @param element the string to check
     * @return true if the element represents the real numbers, false otherwise
     */
    public static boolean isReal(@NotNull String element) {
        return element.equals("(-∞,∞)") || element.equals("(-∞,+∞)") || element.equals(SetSymbols.REAL.toString());
    }

    /**
     * Converts a display string to the corresponding SetSymbols enum value.
     *
     * @param display the display string
     * @return the corresponding SetSymbols value, or NO_OPERATION if not found
     */
    public static SetSymbols fromDisplay(String display) {
        return isReal(display) ? REAL : DISPLAY_LOOKUP.getOrDefault(display, NO_OPERATION);
    }

    /**
     * Checks if the given element is contained in the display lookup map.
     *
     * @param element the element to check
     * @return true if the element exists in the display lookup, false otherwise
     */
    public static boolean equals(String element) {
        return DISPLAY_LOOKUP.containsKey(element);
    }

    @Override
    public String toString() {
        return displaySymbol;
    }

}

