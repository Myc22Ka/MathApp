package pl.myc22ka.mathapp.modifier.model.modifiers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing template modifiers.
 * <p>
 * These modifiers depend on additional user-provided information.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
@Getter
@RequiredArgsConstructor
public enum Template {

    /**
     * Disjoint sets allowed.
     */
    DISJOINT_SETS(1);

    private final int code;
}
