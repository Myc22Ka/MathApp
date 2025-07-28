package pl.myc22ka.mathapp.model.set.sets;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.exceptions.ServerError;
import pl.myc22ka.mathapp.exceptions.ServerErrorMessages;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.ISetType;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.visitors.SetVisitor;
import pl.myc22ka.mathapp.model.set.visitors.DifferenceVisitor;
import pl.myc22ka.mathapp.model.set.visitors.IntersectionVisitor;
import pl.myc22ka.mathapp.model.set.visitors.UnionVisitor;

import static pl.myc22ka.mathapp.model.set.SetSymbols.EMPTY;
import static pl.myc22ka.mathapp.model.set.SetSymbols.REAL;

/**
 * Mathematical interval set ℝ.
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 2025.06.19
 */
public class Fundamental implements ISet {
    private final IExpr expression;

    @Getter
    @Setter
    private SetSymbols leftSymbol;

    /**
     * Creates a new Fundamental set based on a predefined symbol.
     *
     * <p>Currently supports only the symbols {@link SetSymbols#REAL} and {@link SetSymbols#EMPTY}.
     * Attempting to use other symbols will result in an error.</p>
     *
     * @param symbol the predefined symbol representing the fundamental set
     * @throws ServerError if the provided symbol is not supported for construction
     */
    public Fundamental(@NotNull SetSymbols symbol) {

        if (symbol != REAL && symbol != EMPTY) {
            throw new ServerError(ServerErrorMessages.UNSUPPORTED_CONSTRUCTION_BUILD);
        }

        this.leftSymbol = symbol;
        this.expression = leftSymbol.parse();
    }

    @Override
    public @NotNull IExpr getExpression() {
        return expression;
    }

    @Override
    public @NotNull ISetType getISetType() {
        return ISetType.FUNDAMENTAL;
    }

    @Override
    public boolean contains(@NotNull String element) {
        return leftSymbol.contains(element);
    }

    @Override
    public <T> T accept(@NotNull SetVisitor<T> visitor) {
        return visitor.visitFundamental(this);
    }

    @Override
    public void remove(@NotNull String element) {
        throw new ServerError(ServerErrorMessages.UNSUPPORTED_OPERATION);
    }

    @Override
    public Integer size() {
        return leftSymbol == EMPTY ? 0 : null;
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

    @Override
    public Interval toInterval() {
        if(leftSymbol.equals(REAL)){
            return new Interval(F.Infinity, BoundType.OPEN, BoundType.OPEN, F.CNInfinity);
        }

        return ISet.super.toInterval();
    }

    @Override
    public String toString() {
        return leftSymbol.toString();
    }
}
