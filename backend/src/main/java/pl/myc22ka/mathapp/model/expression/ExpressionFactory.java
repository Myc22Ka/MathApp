package pl.myc22ka.mathapp.model.expression;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.dto.ContextRecord;
import pl.myc22ka.mathapp.model.set.SetFactory;

import java.util.List;

/**
 * Factory class responsible for parsing strings into MathExpression objects.
 * <p>
 * It holds a list of expression parsers and delegates the parsing task
 * to the first parser that can handle the given input.
 *
 * @author Myc22Ka
 * @version 1.1.0
 * @since 11.08.2025
 */
@Component
public class ExpressionFactory {

    private final List<IExpressionParser<? extends MathExpression>> parsers = List.of(
            new SetFactory()
            // TODO: I need to add here next parsers if I create them :)
    );

    /**
     * Parse math expression.
     *
     * @param expression the expression
     * @return the math expression
     */
    @Deprecated
    public MathExpression parse(@NotNull String expression) {
        String trimmed = expression.replaceAll("\\s+", "");

        for (IExpressionParser<?> parser : parsers) {
            if (parser.canHandle(trimmed)) {
                return parser.parse(trimmed);
            }
        }

        throw new IllegalArgumentException("Unsupported expression: " + expression);
    }

    public MathExpression parse(@NotNull ContextRecord contextRecord) {
        String trimmedValue = contextRecord.value().replaceAll("\\s+", "");

        for (IExpressionParser<?> parser : parsers) {
            if(parser.getPrefix() == contextRecord.key().prefix() && parser.canHandle(trimmedValue)) {
                return parser.parse(trimmedValue);
            }
        }

        throw new IllegalArgumentException("Unsupported expression: " + contextRecord.value());
    }
}
