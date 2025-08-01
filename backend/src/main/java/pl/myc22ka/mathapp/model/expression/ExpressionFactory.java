package pl.myc22ka.mathapp.model.expression;

import pl.myc22ka.mathapp.model.set.SetFactory;

import java.util.List;

public class ExpressionFactory {

    private final List<IExpressionParser<? extends MathExpression>> parsers = List.of(
            new SetFactory()
            // TODO: I need to add here next parsers if I create them :)
    );

    public MathExpression parse(String expression) {
        String trimmed = expression.replaceAll("\\s+", "");

        for (IExpressionParser<?> parser : parsers) {
            if (parser.canHandle(trimmed)) {
                return parser.parse(trimmed);
            }
        }

        throw new IllegalArgumentException("Unsupported expression: " + expression);
    }
}
