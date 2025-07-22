package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.utils.postfix.PostfixConverter;
import pl.myc22ka.mathapp.model.set.utils.postfix.PostfixEvaluator;
import pl.myc22ka.mathapp.model.set.utils.token.Token;
import pl.myc22ka.mathapp.model.set.utils.token.Tokenizer;

import java.util.List;

public class SetParser implements ISetParser{

    @Override
    public boolean canHandle(@NotNull String expression) {
        String trimmed = expression.replaceAll("\\s+", "");
        boolean containsBinaryOps = SetSymbols.getBinaryOperations().stream()
                .anyMatch(op -> trimmed.contains(op.toString()));

        boolean containsFundamental = SetSymbols.getSymbols().stream()
                .anyMatch(symbol -> trimmed.contains(symbol.toString()));

        return containsBinaryOps && !containsFundamental;
    }

    /**
     * Parses and evaluates a set expression with binary operations (∪, △).
     * Handles operator precedence and left-to-right evaluation for same precedence.
     *
     * @param expression the set expression string
     * @return the resulting ISet after applying all operations
     */
    @Override
    public @NotNull ISet parse(@NotNull String expression) {
        String trimmed = expression.replaceAll("\\s+", "");
        List<Token> tokens = Tokenizer.tokenize(trimmed);

        List<Token> postfix = PostfixConverter.toPostfix(tokens);

        return PostfixEvaluator.evaluate(postfix);
    }
}