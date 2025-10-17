package pl.myc22ka.mathapp.utils.resolver.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Represents a template string consisting of a prefix and its identifier.
 * <p>
 * Example: prefix = SET, templateString = "s1".
 * Used as a key in {@link ContextRecord} for placeholder resolution.
 * <p>
 * Provides a helper method to generate a default template string from a prefix.
 *
 * @param templateString the full string used in templates (e.g., "s1")
 * @param prefix         the template prefix (e.g., {@code SET})
 * @author Myc22Ka
 * @since 17.10.2025
 */
public record TemplateString(String templateString, TemplatePrefix prefix) {

    /**
     * Creates a default TemplateString from the given prefix.
     * <p>
     * Automatically generates a string like "{prefix}1".
     *
     * @param prefix the template prefix
     * @return a new TemplateString with "{prefix}1"
     */
    @NotNull
    public static TemplateString fromPrefix(@NotNull TemplatePrefix prefix) {
        String generated = prefix.getKey() + "1";
        return new TemplateString(generated, prefix);
    }
}
