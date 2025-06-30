package pl.myc22ka.mathapp.model.set.visitors;

import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.sets.Finite;
import pl.myc22ka.mathapp.model.set.sets.Fundamental;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.model.set.sets.ReducedFundamental;

/**
 * Visitor interface for performing operations on different {@link ISet} types.
 *
 * @param <T> the return type of the visitor methods
 * @author Myc22Ka
 * @version 1.0
 * @since 2025‑06‑19
 */
public interface SetVisitor<T> {

    /**
     * Visits a {@link Finite} set.
     *
     * @param finite the finite set
     * @return result of the visit
     */
    T visitFinite(Finite finite);

    /**
     * Visits an {@link Interval} set.
     *
     * @param interval the interval set
     * @return result of the visit
     */
    T visitInterval(Interval interval);

    /**
     * Visits a {@link Fundamental} set.
     *
     * @param fundamental the fundamental set
     * @return result of the visit
     */
    T visitFundamental(Fundamental fundamental);

    T visitReducedFundamental(ReducedFundamental reducedFundamental);
}
