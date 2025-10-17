package pl.myc22ka.mathapp.model.expression;

import org.jetbrains.annotations.NotNull;

/**
 * Interface defining a parser for mathematical expressions.
 *
 * @param <T> the specific type of MathExpression this parser produces
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
public interface IExpressionParser<T extends MathExpression> {

    /**
     * Determines whether this parser can handle the given expression string.
     *
     * @param expression the input expression string to check
     * @return true if this parser supports parsing the expression; false otherwise
     */
    boolean canHandle(@NotNull String expression);

    /**
     * Parses the given expression string into a structured MathExpression object.
     *
     * @param expression the input expression string to parse
     * @return the parsed MathExpression of type T
     */
    @NotNull
    T parse(@NotNull String expression);

    /**
     * Returns the {@link TemplatePrefix} associated with this parser.
     *
     * @return the supported {@link TemplatePrefix}
     */
    TemplatePrefix getPrefix();
}
