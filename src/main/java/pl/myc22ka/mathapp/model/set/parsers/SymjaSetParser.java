package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.ExprEvaluator;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.utils.InequalityPattern;

import java.util.regex.Matcher;

import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

public class SymjaSetParser implements ISetParser{

    private final ExprEvaluator evaluator = new ExprEvaluator();

    @Override
    public boolean canHandle(@NotNull String expression) {
        if (expression.equals("True")) return true;

        for (InequalityPattern pattern : InequalityPattern.values()) {
            if (pattern.matches(expression)) return true;
        }
        return false;
    }

    @Override
    public @NotNull ISet parse(@NotNull String expression) {
        if (expression.equals("True")) {
            return new Fundamental(REAL);
        }

        for (InequalityPattern pattern : InequalityPattern.values()) {
            Matcher matcher = pattern.matcher(expression);
            if (matcher.matches()) {
                String value = matcher.group(1);
                return pattern.toInterval(value, evaluator);
            }
        }

        throw new IllegalArgumentException("Unsupported inequality expression: " + expression);
    }
}
