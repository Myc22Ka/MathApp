package pl.myc22ka.mathapp.utils;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.S;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import pl.myc22ka.mathapp.exceptions.FunctionErrorMessages;
import pl.myc22ka.mathapp.exceptions.FunctionException;
import pl.myc22ka.mathapp.utils.functions.ConditionRoots;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MathUtils {
    private static final ExprEvaluator evaluator = new ExprEvaluator();

    public static IExpr getVariableBelongsToReals(ISymbol variable) {
        ISymbol symbol = variable != null ? variable : F.x;
        return F.Element(symbol, F.Dummy("ℝ"));
    }

    /**
     * Extracts numerical roots from a Symja expression returned by the `Solve`
     * function.
     *
     * @param expr Symja expression containing equation solutions.
     * @return List of extracted roots as strings.
     */
    public static List<IExpr> getRootsFromExpr(IExpr expr) {
        List<IExpr> roots = new ArrayList<>();

        // No solution check
        if (expr.isList() && expr.size() == 1) {
            throw new FunctionException(FunctionErrorMessages.NO_SOLUTIONS);
        }

        // All solution check
        if(expr.isList() && expr.size() == 2){
            throw new FunctionException(FunctionErrorMessages.ALL_SOLUTIONS);
        }

        if (expr.isList()) {
            for (int i = 0; i < expr.size(); i++) {
                IExpr solution = expr.getAt(i);

                if (!solution.isList())
                    continue;

                for (int j = 0; j < solution.size(); j++) {
                    IExpr result = solution.getAt(j);

                    if (!result.isRuleAST())
                        continue;

                    IExpr rhs = result.second();

                    roots.add(rhs);
                }
            }
        }

        return roots;
    }

    /**
     * Detect the first variable used in a Symja expression.
     * If none are found, defaults to F.x.
     *
     * @param expr the expression to analyze
     * @return the first variable found (as ISymbol), or F.x as default
     */
    public static ISymbol detectFirstVariable(IExpr expr) {
        IExpr result = evaluator.eval(F.Variables(expr));

        if (result.isList() && !result.isEmpty()) {
            IExpr first = result.getAt(1);
            if (first instanceof ISymbol symbol) {
                return symbol;
            }
        }

        return F.x;
    }

    public static ISymbol detectFirstVariable(String rawExpression) {
        ExprEvaluator evaluator = new ExprEvaluator();
        IExpr expr = evaluator.parse(rawExpression);
        return detectFirstVariable(expr);
    }

    public static List<ConditionRoots> getConditionsRootsFromExpr(IExpr expr) {
        List<ConditionRoots> result = new ArrayList<>();

        // No solution check
        if (expr.isList() && expr.size() == 1) {
            throw new FunctionException(FunctionErrorMessages.NO_SOLUTIONS);
        }

        // All solution check
        if(expr.isList() && expr.size() == 2){
            throw new FunctionException(FunctionErrorMessages.ALL_SOLUTIONS);
        }

        if (expr.isList()) {
            for (int i = 0; i < expr.size(); i++) {
                IExpr solution = expr.getAt(i);

                if (!solution.isList())
                    continue;

                var conditionalExpression = solution.first().second();

                result.add(new ConditionRoots(conditionalExpression.first(), conditionalExpression.second()));
            }
        }

        return result;
    }

    public static List<IExpr> getAllRoots(List<ConditionRoots> conditionRootsList) {
        return conditionRootsList.stream().map(ConditionRoots::root).toList();
    }

    public static List<IExpr> getAllConditions(List<ConditionRoots> conditionRootsList) {
        return conditionRootsList.stream().map(ConditionRoots::root).collect(Collectors.toSet()).stream().toList();
    }
}
