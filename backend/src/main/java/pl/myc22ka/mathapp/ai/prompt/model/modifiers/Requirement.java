package pl.myc22ka.mathapp.ai.prompt.model.modifiers;

/**
 * Enum defining possible requirements for all modifiers.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
public enum Requirement {

    /**
     * Only intervals allowed.
     */
    INTERVALS_ONLY,

    /**
     * Only finite sets allowed.
     */
    FINITE_SETS_ONLY,

    /**
     * Only positive values allowed.
     */
    POSITIVE_ONLY,

    /**
     * Sets must be disjoint.
     */
    DISJOINT_SETS
}
