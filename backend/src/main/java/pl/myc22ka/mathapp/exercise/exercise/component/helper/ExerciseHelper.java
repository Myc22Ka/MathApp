package pl.myc22ka.mathapp.exercise.exercise.component.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.service.PromptService;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;
import pl.myc22ka.mathapp.exercise.template.component.TemplateLike;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.step.model.StepWrapper;
import pl.myc22ka.mathapp.step.service.StepExecutorRegistry;
import pl.myc22ka.mathapp.step.service.StepMemoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for working with exercises and templates.
 * Provides methods to fetch, build, and verify exercises.
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 13.09.2025
 */
@Component
@RequiredArgsConstructor
public class ExerciseHelper {

    private final ExerciseRepository exerciseRepository;
    private final TemplateResolver templateResolver;
    private final ExpressionFactory expressionFactory;
    private final PromptService promptService;
    private final StepMemoryService stepMemoryService;
    private final StepExecutorRegistry registry;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Fetches an exercise by ID.
     *
     * @param id the exercise ID
     * @return the found exercise
     * @throws IllegalArgumentException if exercise not found
     */
    public Exercise getExercise(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id " + id));
    }

    /**
     * Extracts placeholders from a template exercise text.
     *
     * @param template the template exercise
     * @return list of placeholder entries
     */
    public List<PrefixModifierEntry> getPlaceholders(@NotNull TemplateLike template) {
        return templateResolver.findPrefixModifiers(template.getTemplateText());
    }

    /**
     * Validates that the number of values matches the number of placeholders.
     *
     * @param placeholders list of placeholders
     * @param values       list of provided values
     * @throws IllegalArgumentException if counts do not match
     */
    public void validatePlaceholderCount(@NotNull List<PrefixModifierEntry> placeholders, @NotNull List<String> values) {
        if (placeholders.size() != values.size()) {
            throw new IllegalArgumentException("Number of values does not match number of placeholders in template");
        }
    }

    /**
     * Builds a context mapping placeholders to their parsed values.
     *
     * @param placeholders list of placeholders
     * @param values       list of raw values
     * @return list of PrefixValue representing the context
     */
    public List<PrefixValue> buildContext(@NotNull List<PrefixModifierEntry> placeholders, List<String> values) {
        List<PrefixValue> context = new ArrayList<>();
        for (int i = 0; i < placeholders.size(); i++) {
            PrefixModifierEntry entry = placeholders.get(i);
            String parsedText = parseValue(values.get(i)).toString();
            values.set(i, parsedText);
            context.add(new PrefixValue(entry.prefix().getKey() + entry.index(), parsedText));
        }
        return context;
    }

    /**
     * Parses a value using the expression factory.
     *
     * @param value the raw value
     * @return parsed {@link MathExpression} representation
     */
    public MathExpression parseValue(String value) {
        return expressionFactory.parse(value);
    }

    /**
     * Resolves the template text with a given context.
     *
     * @param template the template exercise
     * @param context  list of PrefixValue for placeholders
     * @return resolved text
     */
    public String resolveText(@NotNull TemplateLike template, List<PrefixValue> context) {
        return templateResolver.resolve(template.getTemplateText(), context);
    }

    /**
     * Builds a final Exercise object with context JSON instead of values.
     *
     * @param template the template exercise
     * @param context  list of PrefixValue representing the context
     * @param text     resolved exercise text
     * @param verified tells if exercises values are verified to theirs modifiers
     * @return new Exercise
     */
    public Exercise buildExercise(TemplateLike template, List<PrefixValue> context, String text, boolean verified) {
        String contextJson = serializeContext(context);
        String answer = calculateAnswer(template, context);

        Exercise.ExerciseBuilder builder = Exercise.builder()
                .text(text)
                .contextJson(contextJson)
                .verified(verified)
                .rating(1.0)
                .answer(answer);

        if (template instanceof TemplateExercise te) {
            builder.templateExercise(te);
        } else if (template instanceof TemplateExerciseVariant v) {
            builder.templateExerciseVariant(v);
        }

        return builder.build();
    }

    /**
     * Verifies that all placeholders and their modifiers are valid for the given category.
     *
     * @param placeholders list of placeholders
     * @param values       list of values for placeholders
     * @param context      resolved placeholder values
     * @param category     the prompt category for verification
     * @return true if all modifiers are verified successfully, false otherwise
     */
    public boolean verifyPlaceholders(
            @NotNull List<PrefixModifierEntry> placeholders,
            @NotNull List<String> values,
            @NotNull List<PrefixValue> context,
            @NotNull PromptType category
    ) {
        boolean allVerified = true;

        for (int i = 0; i < placeholders.size(); i++) {
            var placeholder = placeholders.get(i);
            String currentValue = values.get(i);

            for (var modifier : placeholder.modifiers()) {
                if (modifier.getTemplateInformation() != null) {
                    var resolved = templateResolver.replaceTemplatePlaceholders(
                            "${" + modifier.getTemplateInformation() + "}", context
                    );
                    modifier.setTemplateInformation(resolved);
                }
            }

            boolean verified = promptService.verifyModifierRequestsWithValue(
                    placeholder.modifiers(),
                    currentValue,
                    category
            );

            if (!verified) {
                allVerified = false;
                break;
            }
        }

        return allVerified;
    }

    public String serializeContext(List<PrefixValue> context) {
        try {
            return objectMapper.writeValueAsString(context);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize exercise context", e);
        }
    }

    public List<PrefixValue> deserializeContext(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize exercise context", e);
        }
    }

    public String calculateAnswer(@NotNull TemplateLike template, List<PrefixValue> context) {
        stepMemoryService.clear();
        stepMemoryService.putAll(context);

        List<PrefixValue> contextList = new ArrayList<>(stepMemoryService.getMemory().values());

        for (StepWrapper step : template.getSteps()) {
            registry.executeStep(step, contextList);
        }

        return contextList.getLast().value();
    }
}


