package pl.myc22ka.mathapp.utils.resolver.component;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.topic.component.helper.TopicHelper;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.modifier.dto.ModifierRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.modifier.model.ModifierPrefix;
import pl.myc22ka.mathapp.topic.model.Topic;
import pl.myc22ka.mathapp.modifier.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.modifier.model.modifiers.RequirementModifier;
import pl.myc22ka.mathapp.modifier.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.modifier.repository.ModifierRepository;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Core component for resolving and processing template placeholders in math expressions.
 * <p>
 * This class replaces placeholders like {@code ${s1}} or {@code ${s1:D1}} in template strings
 * with real values provided in the {@link ContextRecord} list. It also supports resolving
 * template modifiers, nested references (e.g., {@code s2:T1->${s1}}), and validation-related
 * prefix extraction. Remember! do not use template modifiers as first modifiers in the chain,
 * because they require template string to base on to.
 * <p>
 * Example:
 * <pre>
 * Input:  "Union of ${s1} and ${s2}"
 * Output: "Union of (1,4) and (2,5)"
 * </pre>
 *
 * @author Myc22Ka
 * @version 1.1.0
 * @since 11.08.2025
 */
@Component
@RequiredArgsConstructor
public class TemplateResolver {

    private static final Pattern TEMPLATE_PATTERN = createPattern();

    private final ModifierRepository modifierRepository;
    private final TopicHelper topicHelper;

    /**
     * Builds a dynamic regex pattern based on available {@link TemplatePrefix} and {@link ModifierPrefix} values.
     * <p>
     * Supports extended modifier syntax, e.g., {@code s2:T1->${s1}|R1|D2}.
     *
     * @return compiled {@link Pattern} for detecting placeholders
     */
    @NotNull
    private static Pattern createPattern() {
        String templatePrefixes = Arrays.stream(TemplatePrefix.values())
                .map(TemplatePrefix::getKey)
                .collect(Collectors.joining());
        String modifierPrefixes = Arrays.stream(ModifierPrefix.values())
                .map(ModifierPrefix::getKey)
                .collect(Collectors.joining());

        // Example supported formats:
        // ${s1}, ${s1:D1}, ${s2:T1->${s1}}, ${s2:D1|R2}
        String modifierPattern = "([" + modifierPrefixes + "]\\d+(->(\\$\\{[a-zA-Z]\\d+}))?)(\\|[" + modifierPrefixes + "]\\d+(->(\\$\\{[a-zA-Z]\\d+}))?)*";
        String regex = "\\$\\{([" + templatePrefixes + "])(\\d+)(:(" + modifierPattern + "))?}";
        return Pattern.compile(regex);
    }

    /**
     * Resolves complex template placeholders like ${t1:D1|R1} or ${T1->${s1}}
     * using context values and modifiers.
     * <p>
     * Handles multiple occurrences, template references, and fallback values.
     *
     * @param input   text containing template placeholders
     * @param context list of context records with values to insert
     * @return text with all template placeholders resolved
     */
    public String resolve(String input, List<ContextRecord> context) {
        Matcher matcher = TEMPLATE_PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();

        Map<String, Integer> counters = new HashMap<>();

        while (matcher.find()) {
            String key = matcher.group(1) + matcher.group(2);

            List<ContextRecord> matching = context.stream()
                    .filter(pv -> pv.key().templateString().equals(key))
                    .toList();

            int count = counters.getOrDefault(key, 0);
            String replacement;
            if (count < matching.size()) {
                replacement = matching.get(count).value();
                counters.put(key, count + 1);
            } else {
                replacement = "X"; // fallback
            }

            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * Scans the string and extracts all template prefixes with their associated modifiers.
     * <p>
     * Example:
     * <pre>
     * Input:  "${s1:D1|T1->${s2}}"
     * Output: For s1 -> [D1, T1]
     * </pre>
     *
     * @param input template text containing template strings
     * @return list of prefix entries, each with its associated modifiers
     */
    public List<PrefixModifierEntry> findPrefixModifiers(String input) {
        Matcher matcher = TEMPLATE_PATTERN.matcher(input);
        List<PrefixModifierEntry> result = new ArrayList<>();

        while (matcher.find()) {
            String prefixKey = matcher.group(1);
            String index = matcher.group(2);
            String modifiersGroup = matcher.group(4);

            TemplatePrefix prefix = TemplatePrefix.fromKey(prefixKey)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown prefix: " + prefixKey));

            Topic topic = topicHelper.findTopicByType(prefix);

            List<ModifierRequest> foundModifierRequests = new ArrayList<>();

            if (modifiersGroup != null && !modifiersGroup.isEmpty()) {
                String[] parts = modifiersGroup.split("\\|");

                List<String> codes = Arrays.stream(parts)
                        .map(p -> p.contains("->") ? p.split("->", 2)[0].trim() : p.trim())
                        .toList();

                List<Modifier> foundModifiers = modifierRepository.findByTemplateCodeIn(codes);

                for (String part : parts) {
                    String code;
                    String placeholder;

                    if (part.contains("->")) {
                        String[] split = part.split("->", 2);
                        code = split[0].trim();
                        placeholder = split[1].trim(); // tu będzie ${s1}
                        if (placeholder.startsWith("${") && placeholder.endsWith("}")) {
                            placeholder = placeholder.substring(2, placeholder.length() - 1);
                        }
                    } else {
                        placeholder = null;
                        code = part.trim();
                    }

                    String finalPlaceholder = placeholder;
                    foundModifiers.stream()
                            .filter(m -> m.getTemplateCode().equals(code))
                            .filter(m -> m.getTopic().getId().equals(topic.getId()))
                            .findFirst()
                            .ifPresent(m -> {
                                ModifierRequest baseReq = toModifierRequest(m);
                                ModifierRequest req = baseReq.withTemplateInformation(finalPlaceholder);
                                foundModifierRequests.add(req);
                            });
                }
            }

            result.add(new PrefixModifierEntry(prefix, index, foundModifierRequests));
        }
        return result;
    }

    /**
     * Replaces basic placeholders like ${s1}, ${s2} with values from the context.
     * <p>
     * Does not support modifiers or multiple occurrences handling.
     *
     * @param templateText text containing simple placeholders
     * @param context      list of context records with key-value pairs
     * @return text with all simple placeholders replaced
     */
    public String replaceTemplateStrings(String templateText, List<ContextRecord> context) {
        if (templateText == null || context == null || context.isEmpty()) {
            return templateText;
        }

        String result = templateText;

        for (ContextRecord pv : context) {
            if (pv.value() != null) {
                String regex = "\\$\\{" + Pattern.quote(pv.key().templateString()) + "}";
                result = result.replaceAll(regex, Matcher.quoteReplacement(pv.value()));
            }
        }

        return result;
    }

    /**
     * Converts a {@link Modifier} entity into a {@link ModifierRequest}.
     *
     * @param modifier modifier to convert
     * @return converted modifier request
     * @throws IllegalArgumentException if the modifier type is not supported
     */
    @NotNull
    private ModifierRequest toModifierRequest(Modifier modifier) {
        switch (modifier) {
            case DifficultyModifier dm -> {
                return new ModifierRequest("DIFFICULTY", dm.getDifficultyLevel(), null, null, null);
            }
            case RequirementModifier rm -> {
                return new ModifierRequest("REQUIREMENT", null, rm.getRequirement(), null, null);
            }
            case TemplateModifier tm -> {
                String info = tm.getInformation() != null ? tm.getInformation().toString() : null;
                return new ModifierRequest("TEMPLATE", null, null, tm.getTemplate(), info);
            }
            case null, default -> throw new IllegalArgumentException("Unknown Modifier type");
        }
    }

    /**
     * Returns the input string with all template placeholders removed.
     *
     * @param input the input string possibly containing template placeholders like ${s1}, ${s1:D1}, etc.
     * @return input string without any template placeholders
     */
    public String removeTemplatePlaceholders(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        Matcher matcher = TEMPLATE_PATTERN.matcher(input);
        return matcher.replaceAll(""); // usuwa wszystkie dopasowania
    }

    /**
     * Returns all template prefixes found in the input string.
     * Example: for input "Calculate ${s1} and ${s2:D1}", returns ["s1", "s2"].
     *
     * @param input the input string containing template placeholders
     * @return set of template prefix keys found in the input
     */
    public Set<String> findTemplatePrefixes(String input) {
        if (input == null || input.isEmpty()) {
            return Set.of();
        }

        Matcher matcher = TEMPLATE_PATTERN.matcher(input);
        Set<String> prefixes = new LinkedHashSet<>();

        while (matcher.find()) {
            String prefixKey = matcher.group(1);
            String prefixNumber = matcher.group(2);
            prefixes.add(prefixKey + prefixNumber);
        }

        return prefixes;
    }
}