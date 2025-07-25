package pl.myc22ka.mathapp.model.set.utils.token;

/**
 * Enumeration of token types used in the tokenizer.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 23.07.2025
 */
public enum TokenType {
    /**
     * Represents an operand token, such as a set or interval.
     */
    OPERAND,

    /**
     * Represents an operator token, such as union (∪), intersection (∩), or difference (\).
     */
    OPERATOR,

    /**
     * Represents a left parenthesis token '('.
     */
    LEFT_PAREN,

    /**
     * Represents a right parenthesis token ')'.
     */
    RIGHT_PAREN
}