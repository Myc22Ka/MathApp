package pl.myc22ka.mathapp.model.set.utils.postfix;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.*;
import pl.myc22ka.mathapp.model.set.utils.token.Token;
import pl.myc22ka.mathapp.model.set.utils.token.TokenType;

import java.util.*;

/**
 * Utility class for evaluating set expressions written in postfix (Reverse Polish Notation).
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
@UtilityClass
public class PostfixEvaluator {

    /**
     * Evaluates a postfix expression representing a set operation.
     *
     * <p>Each operand in the expression is parsed into an {@link ISet}, and each operator is resolved
     * using the corresponding {@link SetSymbols} operation (e.g. ∪, ∩, ∖).</p>
     *
     * @param postfix the list of tokens in postfix order
     * @return the resulting {@link ISet} after evaluating the expression
     * @throws IllegalArgumentException if the expression is malformed or contains unknown operators
     */
    public @NotNull ISet evaluate(@NotNull List<Token> postfix) {
        Stack<ISet> stack = new Stack<>();

        for (Token token : postfix) {
            if (token.type() == TokenType.OPERAND) {
                stack.push(SetFactory.fromString(token.value()));
            } else if (token.type() == TokenType.OPERATOR) {
                if (stack.size() < 2) throw new IllegalArgumentException("Invalid expression: insufficient operands for " + token.value());
                ISet right = stack.pop();
                ISet left = stack.pop();
                SetSymbols op = SetSymbols.getBinaryOperations().stream()
                        .filter(s -> s.toString().equals(token.value()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown operator: " + token.value()));
                ISet result = switch (op) {
                    case UNION -> left.union(right);
                    case INTERSECTION -> left.intersection(right);
                    case DIFFERENCE -> left.difference(right);
                    default -> throw new IllegalStateException("Unsupported operator: " + token.value());
                };
                stack.push(result);
            }
        }

        if (stack.size() != 1) throw new IllegalArgumentException("Invalid expression: unbalanced operands and operators");
        return stack.pop();
    }
}
