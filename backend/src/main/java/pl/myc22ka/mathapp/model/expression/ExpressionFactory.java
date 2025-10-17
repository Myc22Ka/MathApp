package pl.myc22ka.mathapp.model.expression;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
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
     * Parses a mathematical expression string into a {@link MathExpression} object.
     *
     * @param contextRecord contains both the prefix (type/category) and the raw expression value
     * @return parsed {@link MathExpression} instance
     * @throws IllegalArgumentException if no matching parser can handle the expression
     */
    public MathExpression parse(@NotNull ContextRecord contextRecord) {
        String trimmedValue = contextRecord.value().replaceAll("\\s+", "");

        for (IExpressionParser<?> parser : parsers) {
            if(parser.getPrefix() == contextRecord.key().prefix() && parser.canHandle(trimmedValue)) {
                return parser.parse(trimmedValue);
            }
        }
        // TODO: I need to make sure that parser is chosen by prefix not by order in the list
        //       And make sure that when user gives you values all values are parsed by same parser as provided by template string
        throw new IllegalArgumentException("Unsupported expression: " + contextRecord.value());
    }
}
