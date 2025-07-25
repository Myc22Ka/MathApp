package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.utils.postfix.PostfixConverter;
import pl.myc22ka.mathapp.model.set.utils.postfix.PostfixEvaluator;
import pl.myc22ka.mathapp.model.set.utils.token.Token;
import pl.myc22ka.mathapp.model.set.utils.token.Tokenizer;

import java.util.List;

/**
 * Parser for Multiple set expressions, such as "(-∞,5]∪[10,∞)".
 * It produces a {@link Interval} set representation.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 22.07.2025
 */
public final class SetParser implements ISetParser {

    @Override
    public boolean canHandle(@NotNull String expression) {
        boolean containsBinaryOps = SetSymbols.getBinaryOperations().stream()
                .anyMatch(op -> expression.contains(op.toString()));

        boolean containsFundamental = SetSymbols.getSymbols().stream()
                .anyMatch(symbol -> expression.contains(symbol.toString()));

        return containsBinaryOps && !containsFundamental;
    }

    @Override
    public @NotNull ISet parse(@NotNull String expression) {
        List<Token> tokens = Tokenizer.tokenize(expression);

        List<Token> postfix = PostfixConverter.toPostfix(tokens);

        return PostfixEvaluator.evaluate(postfix);
    }
}