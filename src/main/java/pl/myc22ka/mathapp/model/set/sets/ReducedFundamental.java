package pl.myc22ka.mathapp.model.set.sets;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.ISetType;
import pl.myc22ka.mathapp.model.set.Set;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.visitors.SetVisitor;
import pl.myc22ka.mathapp.model.set.visitors.DifferenceVisitor;
import pl.myc22ka.mathapp.model.set.visitors.IntersectionVisitor;
import pl.myc22ka.mathapp.model.set.visitors.UnionVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Mathematical interval set ℂ\{1,2}, ℝ{1/2}.
 *
 * @author Myc22Ka
 * @version 1.0
 * @since 2025‑06‑19
 */

@Getter
@Setter
public class ReducedFundamental implements ISet {
    private SetSymbols operation;
    private ISet right;
    private ISet left;
    private SetSymbols leftSymbol;
    private boolean parenthesis;

    public ReducedFundamental(@NotNull ISet leftSymbol, @NotNull SetSymbols operation, @NotNull ISet right, boolean parenthesis) {
        this.leftSymbol = SetSymbols.fromDisplay(leftSymbol.toString());
        this.left = leftSymbol;
        this.operation = operation;
        this.right = right;
        this.parenthesis = parenthesis;
    }

    public ISet simplify(){
        if (left instanceof ReducedFundamental || right instanceof ReducedFundamental) {
            return this;
        }

        return switch (operation){
            case DIFFERENCE -> left.difference(right);
            case UNION -> left.union(right);
            case INTERSECTION -> left.intersection(right);
            default -> this;
        };
    }

    @Override
    public @NotNull ISetType getISetType() {
        return ISetType.REDUCED_FUNDAMENTAL;
    }

    @Override
    public boolean contains(@NotNull String element) {
        boolean inLeft = new Fundamental(leftSymbol).contains(element);
        if (inLeft) {
            return switch (operation) {
                case DIFFERENCE -> !right.contains(element);
                case INTERSECTION -> right.contains(element);
                case UNION -> true;
                default -> false;
            };
        }
        return false;
    }

    @Override
    public <T> T accept(@NotNull SetVisitor<T> visitor) {
        return visitor.visitReducedFundamental(this);
    }

    @Override
    public void remove(@NotNull String element) {
        setOperation(SetSymbols.DIFFERENCE);
        setRight(Set.of(element));

        if(leftSymbol.toString().equals(element)){
            setLeftSymbol(SetSymbols.EMPTY);
            setOperation(SetSymbols.NO_OPERATION);
            setRight(Set.of(""));
        }
    }

    @Override
    public Integer size() {
        return null;
    }

    @Override
    public @NotNull ISet union(@NotNull ISet other) {
        return other.accept(new UnionVisitor(this));
    }

    @Override
    public @NotNull ISet difference(@NotNull ISet other) {
        return other.accept(new DifferenceVisitor(this));
    }

    @Override
    public @NotNull ISet intersection(@NotNull ISet other) {
        return other.accept(new IntersectionVisitor(this));
    }

    public String toString() {
        String leftStr = needsParentheses(left) ? "(" + left + ")" : left.toString();
        String rightStr = needsParentheses(right) ? "(" + right + ")" : right.toString();

        return leftStr + operation.toString() + rightStr;
    }

    private boolean needsParentheses(ISet side) {
        if (side instanceof ReducedFundamental rf) {
            return rf.parenthesis;
        }
        return false;
    }
}
