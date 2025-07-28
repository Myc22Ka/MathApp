package pl.myc22ka.mathapp.model.set.utils.postfix;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.utils.token.Token;
import pl.myc22ka.mathapp.model.set.utils.token.TokenType;

import java.util.*;

/**
 * Utility class for converting infix token expressions to postfix (Reverse Polish Notation).
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
@UtilityClass
public class PostfixConverter {

    /**
     * Converts a list of {@link Token} in infix notation to postfix (RPN) using the Shunting Yard algorithm.
     *
     * <p>This method supports operands, operators, and parentheses, and respects operator precedence.</p>
     *
     * @param tokens the list of tokens in infix order
     * @return a new list of tokens in postfix (RPN) order
     */
    public @NotNull List<Token> toPostfix(@NotNull List<Token> tokens) {
        List<Token> output = new ArrayList<>();
        Stack<Token> stack = new Stack<>();

        for (Token token : tokens) {
            switch (token.type()) {
                case OPERAND -> output.add(token);
                case OPERATOR -> {
                    while (!stack.isEmpty() &&
                            stack.peek().type() == TokenType.OPERATOR &&
                            stack.peek().precedence() >= token.precedence()) {
                        output.add(stack.pop());
                    }
                    stack.push(token);
                }
                case LEFT_PAREN -> stack.push(token);
                case RIGHT_PAREN -> {
                    while (!stack.isEmpty() && stack.peek().type() != TokenType.LEFT_PAREN) {
                        output.add(stack.pop());
                    }
                    if (!stack.isEmpty()) stack.pop();
                }
            }
        }
        while (!stack.isEmpty()) output.add(stack.pop());
        return output;
    }
}

