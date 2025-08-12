package pl.myc22ka.mathapp.model.set;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.expression.IExpressionParser;
import pl.myc22ka.mathapp.model.set.parsers.*;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;

import java.util.List;

import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;

/**
 * Factory class for creating {@link ISet} instances from string expressions.
 * Supports finite sets, intervals, and fundamental sets.
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 2025.06.19
 */
public class SetFactory implements IExpressionParser<ISet> {

    private static final List<ISetParser> parsers = List.of(
            new SetParser(),
            new FiniteParser(),
            new IntervalParser(),
            new ReducedFundamentalParser(),
            new SymjaSetParser()
    );

    @Override
    public boolean canHandle(@NotNull String expression) {
        return parsers.stream().anyMatch(p -> p.canHandle(expression));
    }

    @Override
    public @NotNull ISet parse(@NotNull String expression) {
        for (ISetParser parser : parsers) {
            if (parser.canHandle(expression)) {
                return parser.parse(expression);
            }
        }

        throw new IllegalArgumentException("Unsupported set expression: " + expression);
    }
}