package pl.myc22ka.mathapp.model.expression;

import org.matheclipse.core.interfaces.IExpr;

/**
 * Interface representing a mathematical expression.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
public interface MathExpression {
    /**
     * Returns the underlying symbolic expression representation.
     *
     * @return the symbolic expression as an IExpr instance
     */
    IExpr getExpression();

    /**
     * Returns the size or complexity measure of the expression.
     *
     * @return an integer representing the size
     * @throws UnsupportedOperationException if not supported
     */
    default Integer size() {
        throw new UnsupportedOperationException("size() not supported for this expression");
    }

    /**
     * Checks if the expression contains only positive elements.
     *
     * @return true if only positive elements, false otherwise
     * @throws UnsupportedOperationException if not supported
     */
    default boolean onlyPositiveElements() {
        throw new UnsupportedOperationException("onlyPositiveElements() not supported for this expression");
    }

    /**
     * Returns the template prefix associated with this expression.
     *
     * @return the TemplatePrefix of this expression
     * @throws UnsupportedOperationException if not supported
     */
    default TemplatePrefix getTemplatePrefix() {
        throw new UnsupportedOperationException("getTemplatePrefix() not supported for this expression");
    }

    /**
     * Returns if both math expressions are equal
     *
     * @return true if they are the same logically or just by checking text
     * @throws UnsupportedOperationException if not supported
     */
    default boolean equals(MathExpression other){
        throw new UnsupportedOperationException("equals() not supported for this expression");
    }
}