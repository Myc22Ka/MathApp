package pl.myc22ka.mathapp.model.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Enum representing different template prefixes used to identify expression types.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Getter
@RequiredArgsConstructor
public enum TemplatePrefix {
    /**
     * Prefix representing a set expression.
     */
    SET("s"),

    /**
     * Prefix representing a function expression.
     */
    FUNCTION("f");

    private final String key;

    private static final Map<String, TemplatePrefix> lookup = Arrays.stream(values())
            .collect(Collectors.toMap(TemplatePrefix::getKey, e -> e));


    /**
     * Retrieves the TemplatePrefix enum instance corresponding to the given key.
     *
     * @param key the key string
     * @return an Optional containing the TemplatePrefix if found, or empty if not
     */
    @NotNull
    public static Optional<TemplatePrefix> fromKey(String key) {
        return Optional.ofNullable(lookup.get(key));
    }
}