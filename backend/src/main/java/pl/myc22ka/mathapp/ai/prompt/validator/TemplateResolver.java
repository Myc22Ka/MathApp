package pl.myc22ka.mathapp.ai.prompt.validator;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.myc22ka.mathapp.model.expression.TemplatePrefix.SET;

/**
 * The type Template resolver.
 * <p>
 * Responsible for resolving template placeholders in the form `${prefix:}` within the input string,
 * replacing them with corresponding values from the provided context map.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Component
public class TemplateResolver {

    private static final Pattern TEMPLATE_PATTERN = Pattern.compile("\\$\\{([a-zA-Z]+):}");

    private final Map<String, BiFunction<String, Map<String, Object>, String>> resolvers = new HashMap<>();

    /**
     * Instantiates a new Template resolver.
     * <p>
     * Initializes the map of resolvers with predefined handlers for each supported prefix.
     */
    public TemplateResolver() {

        // Resolver for the "s" prefix (e.g. ${s:}), returning the value from context or "X" if absent
        resolvers.put(SET.getKey(), (key, ctx) -> {
            Object val = ctx.get(SET.getKey());
            return val != null ? val.toString() : "X";
        });

        // Additional resolvers can be added here, e.g. for "f" or "a" prefixes
        // resolvers.put("f", (key, ctx) -> ctx.getOrDefault("f", "X").toString());
        // resolvers.put("a", (key, ctx) -> ctx.getOrDefault("a", "X").toString());
    }

    /**
     * Resolves all template placeholders in the input string by replacing them with
     * corresponding values provided in the context map.
     *
     * @param input   the input string containing template placeholders
     * @param context the context map holding keys and their replacement values
     * @return the input string with all placeholders resolved
     */
    public String resolve(String input, Map<String, Object> context) {
        Matcher matcher = TEMPLATE_PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            BiFunction<String, Map<String, Object>, String> resolver = resolvers.get(key);
            String replacement = (resolver != null)
                    ? resolver.apply(key, context)
                    : matcher.group(0);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}