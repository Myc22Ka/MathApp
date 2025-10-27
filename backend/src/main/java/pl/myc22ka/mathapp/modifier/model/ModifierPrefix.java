package pl.myc22ka.mathapp.modifier.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing prefixes for modifier types.
 * <p>
 * Used in template strings to identify modifiers:
 * <ul>
 *     <li>DIFFICULTY → "D"</li>
 *     <li>REQUIREMENT → "R"</li>
 *     <li>TEMPLATE → "T"</li>
 * </ul>
 * Example in template: ${s1:D1} → "D1" is a Difficulty modifier.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Getter
@RequiredArgsConstructor
public enum ModifierPrefix {
    DIFFICULTY("D"),
    REQUIREMENT("R"),
    TEMPLATE("T");

    private final String key;

    public static ModifierPrefix fromTemplateCode(String code) {
        if (code == null || code.isEmpty()) return null;

        String prefix = String.valueOf(code.charAt(0));

        for (ModifierPrefix value : values()) {
            if (value.key.equalsIgnoreCase(prefix)) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unknown modifier prefix: " + prefix);
    }
}
