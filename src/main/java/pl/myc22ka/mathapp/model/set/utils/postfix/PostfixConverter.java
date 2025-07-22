package pl.myc22ka.mathapp.model.set.utils.postfix;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.utils.token.Token;
import pl.myc22ka.mathapp.model.set.utils.token.TokenType;

import java.util.*;

public class PostfixConverter {
    public static @NotNull List<Token> toPostfix(@NotNull List<Token> tokens) {
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

