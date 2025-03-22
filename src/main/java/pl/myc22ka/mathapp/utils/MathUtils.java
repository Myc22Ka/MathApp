package pl.myc22ka.mathapp.utils;

import org.matheclipse.core.interfaces.IExpr;

import java.util.ArrayList;
import java.util.List;

public class MathUtils {
    public static List<Double> createInfiniteRange() {
        return List.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Extracts numerical roots from a Symja expression returned by the `Solve` function.
     *
     * @param expr Symja expression containing equation solutions.
     * @return List of extracted roots as strings.
     */
    public static List<String> extractRootsFromExpr(IExpr expr) {
        List<String> roots = new ArrayList<>();

        if (expr.isList()) {
            for (int i = 0; i < expr.size(); i++) {
                IExpr solution = expr.getAt(i);

                if(!solution.isList()) continue;

                for (int j = 0; j < solution.size(); j++) {
                    IExpr result = solution.getAt(j);

                    if(result.isRuleAST()){
                        String valueStr = result.second().toString();

                        // Remove surrounding parentheses using regex
                        valueStr = valueStr.replaceAll("^\\((.*)\\)$", "$1");

                        roots.add(valueStr);
                    }
                }

            }
        }

        return roots;
    }
}
