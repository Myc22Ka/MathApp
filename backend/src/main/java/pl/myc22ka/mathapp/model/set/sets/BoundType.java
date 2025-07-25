package pl.myc22ka.mathapp.model.set.sets;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Enum representing bound types for intervals: open or closed.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 2025.06.19
 */
@Getter
public enum BoundType {
    /**
     * Open bound type.
     */
    OPEN("Less"),        // (
    /**
     * Closed bound type.
     */
    CLOSED("LessEqual"); // [

    private final String inclusive;

    BoundType(String inclusive) {
        this.inclusive = inclusive;
    }

    /**
     * Converts a string to a BoundType.
     *
     * @param symjaName symbolic name
     * @return matching BoundType or OPEN if none match
     */
    public static BoundType fromInclusive(String symjaName) {
        for (BoundType b : values()) {
            if (b.inclusive.equals(symjaName)) {
                return b;
            }
        }
        return OPEN;
    }

    @Override
    public String toString() {
        return inclusive;
    }

    /**
     * Returns the bracket symbol based on bound type and side.
     *
     * @param isLeft true for left bound, false for right
     * @return bracket symbol
     */
    public @NotNull String toBracket(boolean isLeft) {
        return isLeft
                ? (this == CLOSED ? "[" : "(")
                : (this == CLOSED ? "]" : ")");
    }
}