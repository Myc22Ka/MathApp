package pl.myc22ka.mathapp.utils.resolver.dto;

import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Represents a single context entry used for template resolution.
 * <p>
 * Holds a {@link TemplateString} key (e.g., {@code s1}) and its resolved string value (e.g., {@code (1,4)}).
 * Used to replace placeholders like {@code ${s1}} in template text.
 *
 * @param key   the template key (contains prefix and identifier, e.g., s1)
 * @param value the value to replace the placeholder with
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
public record ContextRecord(TemplateString key, String value) {

    /**
     * Creates a new ContextRecord with a string key and prefix.
     *
     * @param key    the template key (e.g., "1" for s1)
     * @param prefix the template prefix (e.g., {@code S})
     * @param value  the replacement value
     */
    public ContextRecord(String key, TemplatePrefix prefix, String value) {
        this(new TemplateString(key, prefix), value);
    }

    /**
     * Creates a new ContextRecord with only a prefix (e.g., for auto-generated keys).
     *
     * @param prefix the template prefix (e.g., {@code S})
     * @param value  the replacement value
     */
    public ContextRecord(TemplatePrefix prefix, String value) {
        this(TemplateString.fromPrefix(prefix), value);
    }
}