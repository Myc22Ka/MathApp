package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.sets.Finite;
import pl.myc22ka.mathapp.model.set.utils.ExpressionUtils;

public class FiniteParser implements ISetParser {

    @Override
    public boolean canHandle(@NotNull String expr) {
        return expr.matches("\\{.*}") && !ExpressionUtils.containsBinaryOperators(expr);
    }

    @Override
    public @NotNull ISet parse(@NotNull String expr) {
        return new Finite(expr);
    }
}
