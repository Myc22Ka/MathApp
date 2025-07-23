package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;

/**
 * Interface for parsing set expressions into {@link ISet} objects.
 * <p>
 * Implementations are responsible for determining whether they can handle
 * a given expression and for parsing it into a structured set representation.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
public sealed interface ISetParser permits FiniteParser, IntervalParser, SymjaSetParser, ReducedFundamentalParser, SetParser {

    /**
     * Checks if this parser is capable of handling the given set expression.
     *
     * @param expression the string representation of a set expression
     * @return true if this parser can parse the expression, false otherwise
     */
    boolean canHandle(@NotNull String expression);

    /**
     * Parses the given set expression into an {@link ISet} object.
     *
     * @param expression the string representation of a set
     * @return a parsed {@link ISet} representing set
     * @throws IllegalArgumentException if the expression cannot be parsed
     */
    @NotNull
    ISet parse(@NotNull String expression);
}