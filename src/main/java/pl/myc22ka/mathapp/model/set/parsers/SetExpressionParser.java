package pl.myc22ka.mathapp.model.set.parsers;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetFactory;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import java.util.*;

public class SetExpressionParser {
    /**
     * Parses and evaluates a set expression with binary operations (∪, △).
     * Handles operator precedence and left-to-right evaluation for same precedence.
     *
     * @param expression the set expression string
     * @return the resulting ISet after applying all operations
     */
    public static @NotNull ISet parseAndEvaluate(@NotNull String expression) {
        // Remove all whitespace for consistent parsing
        String trimmed = expression.replaceAll("\\s+", "");
        // Tokenize the expression into operands and operators
        List<Token> tokens = tokenize(trimmed);

//        // Debug: print tokens
//        System.out.println("Tokens:");
//        for (Token token : tokens) {
//            System.out.println("  " + token);
//        }

        // Convert to postfix notation (Reverse Polish Notation) to handle precedence
        List<Token> postfix = infixToPostfix(tokens);

//        // Debug: print postfix
//        System.out.println("Postfix:");
//        for (Token token : postfix) {
//            System.out.println("  " + token);
//        }

        // Evaluate the postfix expression
        return evaluatePostfix(postfix);
    }

    /**
     * New approach: scan for intervals first, mark their positions, then tokenize
     */
    private static @NotNull List<Token> tokenize(@NotNull String expression) {
        List<Token> tokens = new ArrayList<>();
        boolean[] isIntervalChar = markIntervalCharacters(expression);

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            // Skip if this character is part of an interval
            if (isIntervalChar[i] && (c == '(' || c == ')')) {
                // Find the complete interval operand
                int start = findIntervalStart(expression, i, isIntervalChar);
                int end = findIntervalEnd(expression, start, isIntervalChar);

                if (start < i) {
                    // We're in the middle of an interval, skip to after it
                    i = end;
                    continue;
                } else {
                    // We're at the start of an interval
                    String operand = expression.substring(start, end);
                    tokens.add(new Token(TokenType.OPERAND, operand, 0));
                    i = end;
                    continue;
                }
            }

            // Binary operator symbols
            if (SetSymbols.getBinaryOperationsString().indexOf(c) != -1) {
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c), 1));
                i++;
            }
            // Grouping parentheses
            else if (c == '(' && !isIntervalChar[i]) {
                tokens.add(new Token(TokenType.LEFT_PAREN, "(", 0));
                i++;
            }
            else if (c == ')' && !isIntervalChar[i]) {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")", 0));
                i++;
            }
            else {
                // Extract operand (set notation, interval, etc.)
                int start = i;
                i = findOperandEnd(expression, i, isIntervalChar);
                if (i > start) {
                    String operand = expression.substring(start, i);
                    tokens.add(new Token(TokenType.OPERAND, operand, 0));
                } else {
                    throw new IllegalArgumentException("Unable to parse character at position " + i + ": '" + c + "'");
                }
            }
        }
        return tokens;
    }

    /**
     * Pre-scan the expression to mark which characters belong to intervals
     */
    private static boolean[] markIntervalCharacters(@NotNull String expression) {
        boolean[] isInterval = new boolean[expression.length()];

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == '[' || c == '(') {
                // Check if this starts an interval
                IntervalInfo info = analyzeInterval(expression, i);
                if (info.isInterval) {
                    // Mark all characters from start to end as part of interval
                    for (int j = info.start; j < info.end; j++) {
                        isInterval[j] = true;
                    }
                }
            }
        }

        return isInterval;
    }

    /**
     * Analyze if position starts an interval and return its boundaries
     */
    private static IntervalInfo analyzeInterval(@NotNull String expression, int pos) {
        char startChar = expression.charAt(pos);
        if (startChar != '[' && startChar != '(') {
            return new IntervalInfo(false, pos, pos);
        }

        int i = pos + 1;
        int depth = 1;
        int commaCount = 0;
        boolean hasSetOps = false;
        char expectedClose = (startChar == '[') ? ']' : ')';

        while (i < expression.length() && depth > 0) {
            char c = expression.charAt(i);

            if (c == '[' || c == '(') {
                depth++;
            } else if (c == ']' || c == ')') {
                depth--;
                if (depth == 0) {
                    // Check if this could be interval notation
                    if (commaCount == 1 && !hasSetOps) {
                        // Valid interval: exactly one comma, no set operations
                        return new IntervalInfo(true, pos, i + 1);
                    } else {
                        return new IntervalInfo(false, pos, pos);
                    }
                }
            } else if (depth == 1) {
                if (c == ',') {
                    commaCount++;
                } else if (SetSymbols.getBinaryOperationsString().indexOf(c) != -1) {
                    hasSetOps = true;
                }
            }
            i++;
        }

        return new IntervalInfo(false, pos, pos);
    }

    private static class IntervalInfo {
        boolean isInterval;
        int start;
        int end;

        IntervalInfo(boolean isInterval, int start, int end) {
            this.isInterval = isInterval;
            this.start = start;
            this.end = end;
        }
    }

    private static int findIntervalStart(@NotNull String expression, int pos, boolean[] isInterval) {
        // Go backwards to find the start of this interval
        int i = pos;
        while (i >= 0 && isInterval[i]) {
            i--;
        }
        return i + 1;
    }

    private static int findIntervalEnd(@NotNull String expression, int start, boolean[] isInterval) {
        // Go forwards to find the end of this interval
        int i = start;
        while (i < expression.length() && isInterval[i]) {
            i++;
        }
        return i;
    }

    /**
     * Find end of operand starting at given position
     */
    private static int findOperandEnd(@NotNull String expression, int start, boolean[] isInterval) {
        int i = start;
        char startChar = expression.charAt(start);

        if (startChar == '{') {
            // Set notation
            int braceCount = 1;
            i++;
            while (i < expression.length() && braceCount > 0) {
                if (expression.charAt(i) == '{') braceCount++;
                else if (expression.charAt(i) == '}') braceCount--;
                i++;
            }
            return i;
        } else if (startChar == '[' && isInterval[start]) {
            // Interval starting with [
            return findIntervalEnd(expression, start, isInterval);
        } else if (startChar == '(' && isInterval[start]) {
            // Interval starting with (
            return findIntervalEnd(expression, start, isInterval);
        } else {
            // Regular operand - continue until operator or grouping paren
            while (i < expression.length()) {
                char c = expression.charAt(i);
                if (SetSymbols.getBinaryOperationsString().indexOf(c) != -1) {
                    break;
                }
                if ((c == '(' || c == ')') && !isInterval[i]) {
                    break;
                }
                i++;
            }
            return i;
        }
    }

    /**
     * Converts infix notation to postfix notation using Shunting Yard algorithm
     */
    private static @NotNull List<Token> infixToPostfix(@NotNull List<Token> tokens) {
        List<Token> output = new ArrayList<>();
        Stack<Token> operators = new Stack<>();

        for (Token token : tokens) {
            switch (token.type) {
                case OPERAND:
                    output.add(token);
                    break;
                case OPERATOR:
                    while (!operators.isEmpty() &&
                            operators.peek().type == TokenType.OPERATOR &&
                            operators.peek().precedence >= token.precedence) {
                        output.add(operators.pop());
                    }
                    operators.push(token);
                    break;
                case LEFT_PAREN:
                    operators.push(token);
                    break;
                case RIGHT_PAREN:
                    while (!operators.isEmpty() && operators.peek().type != TokenType.LEFT_PAREN) {
                        output.add(operators.pop());
                    }
                    if (!operators.isEmpty()) {
                        operators.pop(); // Remove the left parenthesis
                    }
                    break;
            }
        }

        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;
    }

    /**
     * Evaluates a postfix expression
     */
    private static @NotNull ISet evaluatePostfix(@NotNull List<Token> postfix) {
        Stack<ISet> stack = new Stack<>();

        for (Token token : postfix) {
            if (token.type == TokenType.OPERAND) {
//                System.out.println("Parsing operand: " + token.value);
                ISet set = parseOperand(token.value);
//                System.out.println("Parsed result: " + set);

                // Additional debug - test simple intervals
                if (set.toString().equals("∅")) {
//                    System.out.println("WARNING: Got empty set for operand: " + token.value);
                    // Try to understand why
                    if (token.value.contains("sqrt")) {
//                        System.out.println("This operand contains sqrt() function - SetFactory might not support it");
                    }
                }

                stack.push(set);
            } else if (token.type == TokenType.OPERATOR) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression: insufficient operands for operator " + token.value);
                }
                ISet right = stack.pop();
                ISet left = stack.pop();

                SetSymbols op = SetSymbols.getBinaryOperations().stream()
                        .filter(s -> s.toString().equals(token.value))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown operator: " + token.value));

//                System.out.println("Applying " + op + " to " + left + " and " + right);

                ISet result = switch (op) {
                    case UNION -> left.union(right);
                    case INTERSECTION -> left.intersection(right);
                    case DIFFERENCE -> left.difference(right);
                    default -> throw new IllegalStateException("Unsupported operator: " + token.value);
                };

//                System.out.println("Result: " + result);
                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: unbalanced operands and operators");
        }

        return stack.pop();
    }

    /**
     * Parses a single operand string into an ISet
     */
    private static @NotNull ISet parseOperand(String operand) {
        // Use existing SetFactory logic for parsing individual operands
        try {
            ISet result = SetFactory.fromString(operand);
//            System.out.println("SetFactory.fromString(\"" + operand + "\") = " + result);
            return result;
        } catch (Exception e) {
//            System.out.println("Error parsing operand \"" + operand + "\": " + e.getMessage());
            throw new IllegalArgumentException("Failed to parse operand: " + operand, e);
        }
    }

    /**
     * Token class for representing elements of the expression
     */
    private static class Token {
        TokenType type;
        String value;
        int precedence;

        Token(TokenType type, String value, int precedence) {
            this.type = type;
            this.value = value;
            this.precedence = precedence;
        }

        @Override
        public @NotNull String toString() {
            return String.format("Token{type=%s, value='%s', precedence=%d}", type, value, precedence);
        }
    }

    /**
     * Token types for expression parsing
     */
    private enum TokenType {
        OPERAND,
        OPERATOR,
        LEFT_PAREN,
        RIGHT_PAREN
    }
}