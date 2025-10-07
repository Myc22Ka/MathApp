package pl.myc22ka.mathapp.model.set;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.visitors.*;

/**
 * A wrapper implementation of {@link ISet}. Acts as a unified entry point for all set types.
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 2025.06.19
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Set implements ISet {
    private final ISet delegate;
    private static final SetFactory setFactory = new SetFactory();

    /**
     * Creates a {@link Set} instance from a string expression.
     *
     * @param expr the set expression
     * @return a new {@link Set} instance
     */
    public static @NotNull Set of(String expr) {
        ISet set = setFactory.parse(expr);
        return new Set(set);
    }

    @Override
    public <T> T accept(@NotNull SetVisitor<T> visitor) {
        return delegate.accept(visitor);
    }

    @Override
    public @NotNull IExpr getExpression() {
        return delegate.getExpression();
    }

    @Override
    public @NotNull ISetType getISetType() {
        return delegate.getISetType();
    }

    @Override
    public boolean contains(@NotNull String element) {
        return delegate.contains(element);
    }

    @Override
    public void remove(@NotNull String element) {
        delegate.remove(element);
    }

    @Override
    public Integer size() {
        return delegate.size();
    }

    @Override
    public @NotNull ISet union(@NotNull ISet other) {
        return other.accept(new UnionVisitor(delegate));
    }

    @Override
    public @NotNull ISet difference(@NotNull ISet other) {
        return other.accept(new DifferenceVisitor(delegate));
    }

    @Override
    public @NotNull ISet intersection(@NotNull ISet other) {
        return other.accept(new IntersectionVisitor(delegate));
    }
}

