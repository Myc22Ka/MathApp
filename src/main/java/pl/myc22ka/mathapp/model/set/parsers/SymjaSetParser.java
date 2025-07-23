package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.ExprEvaluator;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.utils.ExpressionUtils;
import pl.myc22ka.mathapp.model.set.utils.InequalityPattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

public class SymjaSetParser implements ISetParser{

    private final ExprEvaluator evaluator = new ExprEvaluator();

    private static final Pattern AND_PATTERN = Pattern.compile("(.+)\\s*∧\\s*(.+)");
    private static final Pattern OR_PATTERN = Pattern.compile("(.+)\\s*∨\\s*(.+)");


    @Override
    public boolean canHandle(@NotNull String expression) {
        if (expression.equals("True")) return true;

        if (AND_PATTERN.matcher(expression).matches() || OR_PATTERN.matcher(expression).matches()) {
            return true;
        }

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

        // *** UŻYCIE ExpressionUtils do usunięcia zewnętrznych nawiasów ***
        expression = ExpressionUtils.stripOuterParentheses(expression.trim());

        // *** ZNAJDŹ GŁÓWNY OPERATOR LOGICZNY używając ExpressionUtils ***
        // Najpierw szukaj ∨ (OR ma niższy priorytet)
        int orPosition = ExpressionUtils.findOperatorPosition(expression, "∨");
        if (orPosition != -1) {
            String leftExpression = expression.substring(0, orPosition).trim();
            String rightExpression = expression.substring(orPosition + 1).trim();

            ISet leftSet = parse(leftExpression);
            ISet rightSet = parse(rightExpression);

            // *** TUTAJ UŻYJ UNION ***
            return leftSet.union(rightSet);
        }

        // Jeśli nie znaleziono ∨, szukaj ∧ (AND)
        int andPosition = ExpressionUtils.findOperatorPosition(expression, "∧");
        if (andPosition != -1) {
            String leftExpression = expression.substring(0, andPosition).trim();
            String rightExpression = expression.substring(andPosition + 1).trim();

            ISet leftSet = parse(leftExpression);
            ISet rightSet = parse(rightExpression);

            // *** TUTAJ UŻYJ INTERSECTION ***
            return leftSet.intersection(rightSet);
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
