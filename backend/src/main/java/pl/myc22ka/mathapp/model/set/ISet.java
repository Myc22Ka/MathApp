package pl.myc22ka.mathapp.model.set;

import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.interfaces.IExpr;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.visitors.SetVisitor;

/**
 * Represents a mathematical set with common set operations.
 *
 * @author Myc22Ka
 * @version 1.0
 * @since 2025 -06-19
 */
public interface ISet {

    /**
     * Accepts a visitor to perform an operation on this set.
     *
     * @param <T>     return type of the visitor
     * @param visitor the set visitor
     * @return result from the visitor
     */
    <T> T accept(@NotNull SetVisitor<T> visitor);

    /**
     * Returns the expression representing this set.
     *
     * @return Symja expression {@code IExpr}
     */
    default IExpr getExpression(){
        return null;
    };

    /**
     * Returns the type of the set.
     *
     * @return set type {@link ISetType}
     */
    @NotNull ISetType getISetType();

    /**
     * Checks if the set contains the given element.
     *
     * @param element element to check
     * @return true if present, false otherwise
     */
    boolean contains(@NotNull String element);

    /**
     * Removes the specified element from the set.
     *
     * @param element element to remove
     */
    void remove(@NotNull String element);

    /**
     * Returns the size of the set, or null if not applicable.
     *
     * @return number of elements or null
     */
    Integer size();

    /**
     * Returns the union of this set and another.
     *
     * @param other the other set
     * @return resulting {@link ISet} set
     */
    @NotNull ISet union(@NotNull ISet other);

    /**
     * Returns the difference between this set and another.
     *
     * @param other the other set
     * @return resulting {@link ISet} set
     */
    @NotNull ISet difference(@NotNull ISet other);

    /**
     * Returns the intersection of this set and another.
     *
     * @param other the other set
     * @return resulting {@link ISet} set
     */
    @NotNull ISet intersection(@NotNull ISet other);

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    default boolean isEmpty(){
        return this.toString().equals(SetSymbols.EMPTY.toString());
    }

    /**
     * Complement set.
     *
     * @param universe the universe
     * @return the set
     */
// A' = U \ A
    default @NotNull ISet complement(@NotNull ISet universe) {
        if (universe.toString().equals(SetSymbols.REAL.toString())){
            return universe.toInterval().difference(this);
        }

        return universe.difference(this);
    }

    /**
     * Symmetric difference set.
     *
     * @param other the other
     * @return the set
     */
    default ISet symmetricDifference(@NotNull ISet other){
        return this.difference(other).union(other.difference(this));
    }

    /**
     * Converts this set to an interval, if supported.
     *
     * @return interval representation
     * @throws UnsupportedOperationException if not supported
     */
    default Interval toInterval() {
        throw new UnsupportedOperationException("This set type cannot be converted to interval.");
    }
}
