package pl.myc22ka.mathapp.model.set.parsers;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.ReducedFundamental;
import pl.myc22ka.mathapp.model.set.utils.ExpressionUtils;

import static pl.myc22ka.mathapp.model.set.ISetType.FUNDAMENTAL;
import static pl.myc22ka.mathapp.model.set.ISetType.INTERVAL;
import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;
import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

public class FundamentalSetParser implements ISetParser {

    private boolean containsSingleBinaryOperator(String expr) {
        var binaryOps = SetSymbols.getBinaryOperations();
        return ExpressionUtils.countTopLevelOperators(expr, binaryOps) == 1;
    }

    @Override
    public boolean canHandle(@NotNull String expr) {
        return SetSymbols.equals(expr) || containsSingleBinaryOperator(expr);
    }

    @Override
    public @NotNull ISet parse(@NotNull String expr) {

        if (SetSymbols.isReal(expr)) return new Fundamental(REAL);

        var binaryOps = SetSymbols.getBinaryOperations();
        for (SetSymbols op : binaryOps) {
            int pos = ExpressionUtils.findOperatorPosition(expr, op.toString());
            if (pos > 0) {
                ISet left = new SetParser().parse(expr.substring(0, pos));
                String rightRaw = expr.substring(pos + op.toString().length());
                String right = ExpressionUtils.stripOuterParentheses(rightRaw);
                ISet rightSet = new SetParser().parse(right);

                if (left.getISetType() == FUNDAMENTAL || left.getISetType() == INTERVAL) {
                    return new ReducedFundamental(left, op, rightSet);
                }
            }
        }
        return new Fundamental(EMPTY);
    }
}