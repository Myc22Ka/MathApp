package pl.myc22ka.mathapp.model.set.utils.token;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.utils.scanner.IntervalScanner;

import java.util.*;

public class Tokenizer {
    public static @NotNull List<Token> tokenize(@NotNull String expression) {
        List<Token> tokens = new ArrayList<>();
        boolean[] isIntervalChar = IntervalScanner.markIntervalCharacters(expression);

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (isIntervalChar[i] && (c == '(' || c == ')')) {
                int start = IntervalScanner.findIntervalStart(expression, i, isIntervalChar);
                int end = IntervalScanner.findIntervalEnd(expression, start, isIntervalChar);
                if (start < i) {
                    i = end;
                    continue;
                } else {
                    tokens.add(new Token(TokenType.OPERAND, expression.substring(start, end), 0));
                    i = end;
                    continue;
                }
            }

            if (SetSymbols.getBinaryOperationsString().indexOf(c) != -1) {
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c), 1));
                i++;
            } else if (c == '(' && !isIntervalChar[i]) {
                tokens.add(new Token(TokenType.LEFT_PAREN, "(", 0));
                i++;
            } else if (c == ')' && !isIntervalChar[i]) {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")", 0));
                i++;
            } else {
                int start = i;
                i = IntervalScanner.findOperandEnd(expression, i, isIntervalChar);
                if (i > start) {
                    tokens.add(new Token(TokenType.OPERAND, expression.substring(start, i), 0));
                } else {
                    throw new IllegalArgumentException("Unexpected character at position " + i);
                }
            }
        }
        return tokens;
    }
}

