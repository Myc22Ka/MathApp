package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.*;
import pl.myc22ka.mathapp.model.set.sets.*;
import pl.myc22ka.mathapp.model.set.utils.ExpressionUtils;

import java.util.List;

import static pl.myc22ka.mathapp.model.set.ISetType.FUNDAMENTAL;
import static pl.myc22ka.mathapp.model.set.ISetType.INTERVAL;
import static pl.myc22ka.mathapp.model.set.SetFactory.fromString;
import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;

public class ReducedFundamentalParser implements ISetParser {
    @Override
    public boolean canHandle(@NotNull String expr) {
        for (SetSymbols symbol : SetSymbols.getBinaryOperations()) {
            int position = ExpressionUtils.findOperatorPosition(expr, symbol.toString());
            if (position > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull ISet parse(@NotNull String trimmed) {
        List<SetSymbols> binaryOperations = SetSymbols.getBinaryOperations();
        for (SetSymbols symbol : binaryOperations) {
            String operator = symbol.toString();
            int position = ExpressionUtils.findOperatorPosition(trimmed, operator);

            if (position <= 0) continue;

            ISet left = fromString(trimmed.substring(0, position));
            String rightExpr = trimmed.substring(position + operator.length());

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