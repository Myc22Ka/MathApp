package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.ExprEvaluator;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.sets.Finite;
import pl.myc22ka.mathapp.model.set.utils.ExpressionUtils;

/**
 * Parser for finite set expressions, such as "{1, 2, 3}".
 * It produces a {@link Finite} set representation.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 22.07.2025
 */
public final class FiniteParser implements ISetParser {

    @Override
    public boolean canHandle(@NotNull String expr) {
        return expr.matches("\\{.*}") && !ExpressionUtils.containsBinaryOperators(expr);
    }

    @Override
    public @NotNull ISet parse(@NotNull String expr) {
        return new Finite(new ExprEvaluator().eval(expr));
    }
}
