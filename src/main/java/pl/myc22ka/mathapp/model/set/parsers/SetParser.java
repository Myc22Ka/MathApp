package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;
import java.util.*;

public class SetParser {
    /**
     * Parses and evaluates a set expression with binary operations (∪, △).
     * Handles operator precedence and left-to-right evaluation for same precedence.
     *
     * @param expression the set expression string
     * @return the resulting ISet after applying all operations
     */
    public static @NotNull ISet parse(@NotNull String expression) {
        String trimmed = expression.replaceAll("\\s+", "");
        List<Token> tokens = Tokenizer.tokenize(trimmed);

        List<Token> postfix = PostfixConverter.toPostfix(tokens);

        return PostfixEvaluator.evaluate(postfix);
    }
}