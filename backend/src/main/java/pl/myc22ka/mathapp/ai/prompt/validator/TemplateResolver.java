package pl.myc22ka.mathapp.ai.prompt.validator;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.ModifierPrefix;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.RequirementModifier;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;
import pl.myc22ka.mathapp.ai.prompt.repository.TopicRepository;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The type Template resolver.
 * <p>
 * Responsible for resolving template placeholders in the form ${prefix:} within the input string,
 * replacing them with corresponding values from the provided context map.
 * Enhanced to support TemplateModifier with set reference resolution.
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 11.08.2025
 */
@Component
@RequiredArgsConstructor
public class TemplateResolver {

    private static final Pattern TEMPLATE_PATTERN = createPattern();

    private final ModifierRepository modifierRepository;
    private final TopicRepository topicRepository;

    /**
     * Creates regex pattern based on available TemplatePrefix enum values.
     * Enhanced to support T1->${s1} format in modifiers section.
     */
    @NotNull
    private static Pattern createPattern() {
        String templatePrefixes = Arrays.stream(TemplatePrefix.values())
                .map(TemplatePrefix::getKey)
                .collect(Collectors.joining());
        String modifierPrefixes = Arrays.stream(ModifierPrefix.values())
                .map(ModifierPrefix::getKey)
                .collect(Collectors.joining());

        // Enhanced regex to support T1->${s1} format:
        // 1. przed dwukropkiem: ([templatePrefixes]\d+)
        // 2. po dwukropku: modifier codes OR template with set reference
        // Modifier może być: D1, R2, T1->${s1}, itp.
        String modifierPattern = "([" + modifierPrefixes + "]\\d+(->(\\$\\{[a-zA-Z]\\d+}))?)(\\|[" + modifierPrefixes + "]\\d+(->(\\$\\{[a-zA-Z]\\d+}))?)*";
        String regex = "\\$\\{([" + templatePrefixes + "])(\\d+)(:(" + modifierPattern + "))?}";
        return Pattern.compile(regex);
    }

    /**
     * Resolves all template placeholders in the input string by replacing them with
     * corresponding values provided in the context map.
     * Enhanced to handle template references like T1->${s1}.
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
            String key = matcher.group(1) + matcher.group(2); // prefix (np. "s1")

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
     * Enhanced to handle TemplateModifier with set reference resolution in format T1->${s1}.
     *
     * @param input   the input string containing template placeholders
     * @return map of prefix -> list of Modifier with resolved information
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

            PromptType type = prefix.toPromptType();

            // Pobranie topic po typie
            Topic topic = topicRepository.findFirstByType(type)
                    .orElseThrow(() -> new IllegalStateException("No Topic found for type " + type));

            List<ModifierRequest> foundModifierRequests = new ArrayList<>();

            if (modifiersGroup != null && !modifiersGroup.isEmpty()) {
                String[] parts = modifiersGroup.split("\\|");

                // --- krok 1: zbieramy wszystkie kody ---
                List<String> codes = Arrays.stream(parts)
                        .map(p -> p.contains("->") ? p.split("->", 2)[0].trim() : p.trim())
                        .toList();

                // --- krok 2: pobieramy modyfikatory z bazy ---
                List<Modifier> foundModifiers = modifierRepository.findByTemplateCodeIn(codes);

                // --- krok 3: mapowanie z obsługą informationPlaceholder ---
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

                                ModifierRequest req = new ModifierRequest(
                                        baseReq.type(),
                                        baseReq.difficultyLevel(),
                                        baseReq.requirement(),
                                        baseReq.template(),
                                        finalPlaceholder // tu trafia to po "->"
                                );

                                foundModifierRequests.add(req);
                            });
                }
            }

            result.add(new PrefixModifierEntry(prefix, index, foundModifierRequests));
        }
        return result;
    }

    /**
     * Prosta metoda do zamiany placeholderów ${key} w templateText na wartości z context.
     *
     * @param templateText tekst zawierający placeholdery ${s1}, ${s2}, itd.
     * @param context      lista PrefixValue z kluczami i wartościami do podmiany
     * @return tekst po podmianie wszystkich kluczy z context
     */
    public String replaceTemplatePlaceholders(String templateText, List<PrefixValue> context) {
        if (templateText == null || context == null || context.isEmpty()) {
            return templateText;
        }

        String result = templateText;

        for (PrefixValue pv : context) {
            if (pv.value() != null) {
                // replaceAll wymaga escapowania dolara i klamry w regexie
                String regex = "\\$\\{" + Pattern.quote(pv.key()) + "}";
                result = result.replaceAll(regex, Matcher.quoteReplacement(pv.value()));
            }
        }

        return result;
    }

    /**
     * Converts regular Modifier to ModifierRequest.
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
}