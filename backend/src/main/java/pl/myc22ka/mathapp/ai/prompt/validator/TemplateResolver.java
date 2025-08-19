package pl.myc22ka.mathapp.ai.prompt.validator;

import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.*;
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
 * @version 1.0.1
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
    public String resolve(String input, List<PrefixValue> context) {
        Matcher matcher = TEMPLATE_PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();

        // Trzymamy wskaźniki dla każdego klucza, ile już razy go użyto
        Map<String, Integer> counters = new HashMap<>();

        while (matcher.find()) {
            String key = matcher.group(1);

            // Filtrujemy listę context, bierzemy tylko te o danym kluczu
            List<PrefixValue> matching = context.stream()
                    .filter(pv -> pv.key().equals(key))
                    .toList();

            int count = counters.getOrDefault(key, 0);
            String replacement;
            if (count < matching.size()) {
                replacement = matching.get(count).value();
                counters.put(key, count + 1); // inkrementujemy licznik dla tego klucza
            } else {
                replacement = "X"; // fallback, jeśli brak wartości
            }

            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Finds all prefixes in the template and maps them to lists of Modifiers.
     * For now this method only detects the prefixes and returns empty lists,
     * since further logic for creating actual Modifier objects will be added later.
     *
     * @param input the input string containing template placeholders
     * @return map of prefix -> list of Modifier (currently empty lists)
     */
    public List<PrefixModifierEntry> findPrefixModifiers(String input) {
        Matcher matcher = TEMPLATE_PATTERN.matcher(input);
        List<PrefixModifierEntry> result = new ArrayList<>();

        while (matcher.find()) {
            String prefixKey = matcher.group(1); // np. "s"
            TemplatePrefix.fromKey(prefixKey).ifPresent(prefix -> {
                result.add(new PrefixModifierEntry(prefix, List.of())); // pusty placeholder
                // TODO: tutaj później dodam ModifierRequest na podstawie template
            });
        }

        return result;
    }

}