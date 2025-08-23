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
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.*;
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
 * Responsible for resolving template placeholders in the form `${prefix:}` within the input string,
 * replacing them with corresponding values from the provided context map.
 *
 * @author Myc22Ka
 * @version 1.0.1
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
     */
    @NotNull
    private static Pattern createPattern() {
        String templatePrefixes = Arrays.stream(TemplatePrefix.values())
                .map(TemplatePrefix::getKey)
                .collect(Collectors.joining());

        String modifierPrefixes = Arrays.stream(ModifierPrefix.values())
                .map(ModifierPrefix::getKey)
                .collect(Collectors.joining());


        // regex:
        // 1. przed dwukropkiem: ([templatePrefixes]\d+)
        // 2. po dwukropku: (:([modifierPrefixes]\d+(\\|[modifierPrefixes]\d+)*))? -> opcjonalnie
        String regex = "\\$\\{([" + templatePrefixes + "])(\\d+)(:(["
                + modifierPrefixes
                + "]\\d+(\\|[" + modifierPrefixes + "]\\d+)*))?}";

        return Pattern.compile(regex);
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
            String key = matcher.group(1); // prefix (np. "s")
            String modifierId = matcher.group(2); // cyfra modyfikatora (np. "1")

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
            String prefixKey = matcher.group(1);
            String modifiersGroup = matcher.group(4);

            TemplatePrefix prefix = TemplatePrefix.fromKey(prefixKey)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown prefix: " + prefixKey));

            PromptType type = prefix.toPromptType();

            // Pobranie topic po typie
            Topic topic = topicRepository.findFirstByType(type)
                    .orElseThrow(() -> new IllegalStateException("No Topic found for type " + type));

            List<ModifierRequest> foundModifierRequests = new ArrayList<>();
            if (modifiersGroup != null && !modifiersGroup.isEmpty()) {
                List<String> codes = Arrays.asList(modifiersGroup.split("\\|"));

                // Pobranie modifierów z bazy
                List<Modifier> foundModifiers = modifierRepository.findByTemplateCodeIn(codes);

                // Filtracja po topic.id + konwersja na ModifierRequest
                foundModifierRequests = foundModifiers.stream()
                        .filter(m -> m.getTopic().getId().equals(topic.getId()))
                        .map(this::toRequest)
                        .toList();
            }

            result.add(new PrefixModifierEntry(prefix, foundModifierRequests));
        }

        return result;
    }


    @NotNull
    private ModifierRequest toRequest(Modifier modifier) {
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
            case null, default ->
                    throw new IllegalArgumentException("Unknown Modifier type");
        }
    }
}