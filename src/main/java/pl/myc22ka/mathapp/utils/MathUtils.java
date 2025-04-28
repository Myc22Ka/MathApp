package pl.myc22ka.mathapp.utils;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.utils.functions.ConditionRoots;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MathUtils {
    public static List<Double> createInfiniteRange() {
        return List.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
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

    public static List<ConditionRoots> getConditionsRootsFromExpr(IExpr expr) {
        List<ConditionRoots> result = new ArrayList<>();

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
