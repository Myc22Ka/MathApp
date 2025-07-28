package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.ReducedFundamental;
import pl.myc22ka.mathapp.model.set.utils.ExpressionUtils;

import java.util.List;

import static pl.myc22ka.mathapp.model.set.ISetType.FUNDAMENTAL;
import static pl.myc22ka.mathapp.model.set.ISetType.INTERVAL;
import static pl.myc22ka.mathapp.model.set.SetFactory.fromString;
import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;
import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

/**
 * Parser for ReducedFundamental set expressions, such as "ℝ∖{-3,1,2}, ℝ∖{3,4}".
 * It produces a {@link ReducedFundamental} set representation.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 22.07.2025
 */
public final class ReducedFundamentalParser implements ISetParser {

    @Override
    public boolean canHandle(@NotNull String expr) {
        var binaryOperations = SetSymbols.getBinaryOperations();

        for (SetSymbols symbol : binaryOperations) {
            int position = ExpressionUtils.findOperatorPosition(expr, symbol.toString());
            if (position > 0) {
                return true;
            }
        }
        return SetSymbols.equals(expr) || ExpressionUtils.countTopLevelOperators(expr, binaryOperations) == 1;
    }

    @Override
    public @NotNull ISet parse(@NotNull String expr) {
        List<SetSymbols> binaryOperations = SetSymbols.getBinaryOperations();

        if (SetSymbols.isReal(expr)) return new Fundamental(REAL);

        for (SetSymbols symbol : binaryOperations) {
            String operator = symbol.toString();
            int position = ExpressionUtils.findOperatorPosition(expr, operator);

            if (position <= 0) continue;

            ISet left = fromString(expr.substring(0, position));
            String rightExpr = expr.substring(position + operator.length());

            rightExpr = ExpressionUtils.stripOuterParentheses(rightExpr);

            ISet right = ExpressionUtils.containsMultipleOperators(rightExpr)
                    ? new SetParser().parse(rightExpr)
                    : fromString(rightExpr);

            if (left.getISetType() == FUNDAMENTAL || left.getISetType() == INTERVAL) {
                return new ReducedFundamental(left, symbol, right);
            }
        }

        return new Fundamental(EMPTY);
    }
}