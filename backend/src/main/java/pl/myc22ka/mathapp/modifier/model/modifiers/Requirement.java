package pl.myc22ka.mathapp.modifier.model.modifiers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum defining possible requirements for all modifiers.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
@Getter
@RequiredArgsConstructor
public enum Requirement {

    /**
     * Only intervals allowed.
     */
    INTERVALS_ONLY(1),

    /**
     * Only finite sets allowed.
     */
    FINITE_SETS_ONLY(2),

    /**
     * Only positive values allowed.
     */
    POSITIVE_ONLY(3),

    /**
     * Sets must be disjoint.
     */
    DISJOINT_SETS(4);

    private final int code;
}
