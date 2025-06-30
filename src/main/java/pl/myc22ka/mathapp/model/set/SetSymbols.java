package pl.myc22ka.mathapp.model.set;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
 * @version 1.0
 * @since 2025‑06‑19
 */
public enum SetSymbols {
    REAL("Reals", "ℝ"),
    COMPLEX("Complexes", "ℂ"),
    INTEGER("Integers", "ℤ"),
    NATURAL("Naturals", "ℕ"),
    RATIONAL("Rationals", "ℚ"),
    EMPTY("EmptySet", "∅"),
    UNION("Union", "∪"),
    INTERSECTION("Intersection", "∩"),
    DIFFERENCE("Complement", "∖"),
    ELEMENT_OF("Element", "∈"),
    SUBSET("Subset", "⊆"),
    PROPER_SUBSET("ProperSubset", "⊂"),
    INFINITY("Infinity", "∞"),
    NEGATIVE_INFINITY("Infinity", "-∞"),
    NO_OPERATION("NoOperation", "NO");


    private final String symjaSymbol;
    private final String displaySymbol;
    private final ExprEvaluator evaluator = new ExprEvaluator();

    private static final Map<String, SetSymbols> SYMBOL_LOOKUP;
    private static final Map<String, SetSymbols> DISPLAY_LOOKUP;

    static {
        Map<String, SetSymbols> symjaMap = new HashMap<>();
        Map<String, SetSymbols> displayMap = new HashMap<>();
        for (SetSymbols symbol : values()) {
            symjaMap.put(symbol.symjaSymbol.toLowerCase(), symbol);
            displayMap.put(symbol.displaySymbol, symbol);
        }
        SYMBOL_LOOKUP = Collections.unmodifiableMap(symjaMap);
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
     * Determines whether this set is a subset of the given set symbolically.
     *
     * @param other the other set symbol
     * @return true if this is a subset of other
     */
    public boolean isSubsetOf(@NotNull SetSymbols other) {
        if (this == other) return true;

        return switch (this) {
            case NATURAL -> other == INTEGER || other == RATIONAL || other == REAL || other == COMPLEX;
            case INTEGER -> other == RATIONAL || other == REAL || other == COMPLEX;
            case RATIONAL -> other == REAL || other == COMPLEX;
            case REAL -> other == COMPLEX;
            case COMPLEX -> false;
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

    public static @NotNull @Unmodifiable List<SetSymbols> getBinaryOperations(){
        return List.of(
                SetSymbols.DIFFERENCE,
                SetSymbols.UNION,
                SetSymbols.INTERSECTION
        );
    }

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
     * Returns string of all binary operations like this: {@code ∖∪∩△}.
     *
     * @return binary operations in single string
     */
    public static @NotNull String getBinaryOperationsString() {
        return getBinaryOperations().stream()
                .map(SetSymbols::toString)
                .collect(Collectors.joining(""));
    }

    public static boolean isReal(String element) {
        return element.equals("(-∞,∞)") || element.equals("(-∞,+∞)") || element.equals(SetSymbols.REAL.toString());
    }

    public static SetSymbols fromDisplay(String display) {
        return isReal(display) ? REAL : DISPLAY_LOOKUP.getOrDefault(display, NO_OPERATION);
    }

    public static boolean equals(String element) {
        return DISPLAY_LOOKUP.containsKey(element);
    }

    @Override
    public String toString() {
        return displaySymbol;
    }

}

