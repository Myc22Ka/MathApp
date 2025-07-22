package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.*;

import java.util.*;

public class PostfixEvaluator {
    public static @NotNull ISet evaluate(@NotNull List<Token> postfix) {
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
