package pl.myc22ka.mathapp.model.expression;

import org.jetbrains.annotations.NotNull;

public interface IExpressionParser<T extends MathExpression> {

    /**
     * Checks if this parser can handle the given expression.
     */
    boolean canHandle(@NotNull String expression);

    /**
     * Parses the expression and returns a structured object.
     */
    @NotNull
    T parse(@NotNull String expression);
}
