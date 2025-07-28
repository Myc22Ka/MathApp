package pl.myc22ka.mathapp.model.set;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.parsers.*;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;

import java.util.List;

import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;

/**
 * Factory class for creating {@link ISet} instances from string expressions.
 * Supports finite sets, intervals, and fundamental sets.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 2025.06.19
 */
public class SetFactory {

    private static final List<ISetParser> parsers = List.of(
            new SetParser(),
            new FiniteParser(),
            new IntervalParser(),
            new ReducedFundamentalParser(),
            new SymjaSetParser()
    );

    /**
     * Parses a set expression and returns the corresponding {@link ISet} implementation.
     *
     * @param setExpression the string representation of the set
     * @return the corresponding {@link ISet} instance
     * @throws IllegalArgumentException if the expression is unsupported
     */
    public static @NotNull ISet fromString(@NotNull String setExpression) {
        String trimmed = setExpression.replaceAll("\\s+", "");

        if (trimmed.isEmpty()) return new Fundamental(EMPTY);

        for (ISetParser parser : parsers) {
            if (parser.canHandle(trimmed)) {
                return parser.parse(trimmed);
            }
        }

        throw new IllegalArgumentException("Unsupported set expression: " + trimmed);
    }
}