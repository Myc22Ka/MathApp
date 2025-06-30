package pl.myc22ka.mathapp.model.set.visitors;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.Finite;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;

import java.util.ArrayList;
import java.util.List;

public class SetUtils {

    /**
     * Zwraca elementy z right, których nie zawiera left.
     */
    public static @NotNull List<IExpr> getExcludedElements(ISet left, @NotNull Finite right) {
        List<IExpr> excluded = new ArrayList<>();
        for (var el : right.exprToList()) {
            if (!left.contains(el.toString())) {
                excluded.add(el);
            }
        }
        return excluded;
    }

    /**
     * Zwraca elementy z right, które zawiera left.
     */
    public static @NotNull List<IExpr> getIncludedElements(ISet left, @NotNull Finite right) {
        List<IExpr> included = new ArrayList<>();
        for (var el : right.exprToList()) {
            if (left.contains(el.toString())) {
                included.add(el);
            }
        }
        return included;
    }

    /**
     * Sprawdza, czy zbiór right jest całkowicie zawarty w zbiorze left.
     */
    public static boolean isCovered(@NotNull Finite right, ISet left) {
        for (var el : right.exprToList()) {
            if (!left.contains(el.toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sprawdza relację zawierania symboli fundamentalnych i zwraca odpowiedni zbiór.
     */
    public static @NotNull ISet resolveFundamentalUnion(@NotNull Fundamental a, @NotNull Fundamental b) {
        if (b.getLeftSymbol().isSubsetOf(a.getLeftSymbol())) return a;
        if (a.getLeftSymbol().isSubsetOf(b.getLeftSymbol())) return b;

        return new Fundamental(SetSymbols.EMPTY);
    }
}
