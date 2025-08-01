package pl.myc22ka.mathapp.model.expression;

import org.matheclipse.core.interfaces.IExpr;

public interface MathExpression {
    IExpr getExpression();

    default Integer size() {
        throw new UnsupportedOperationException("size() not supported for this expression");
    }


}