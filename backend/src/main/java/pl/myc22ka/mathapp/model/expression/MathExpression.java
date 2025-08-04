package pl.myc22ka.mathapp.model.expression;

import org.matheclipse.core.interfaces.IExpr;

public interface MathExpression {
    IExpr getExpression();

    default Integer size() {
        throw new UnsupportedOperationException("size() not supported for this expression");
    }

    default boolean onlyPositiveElements() {
        throw new UnsupportedOperationException("onlyPositiveElements() not supported for this expression");
    }

    default TemplatePrefix getTemplatePrefix(){
        throw new UnsupportedOperationException("getTemplatePrefix() not supported for this expression");
    }
}