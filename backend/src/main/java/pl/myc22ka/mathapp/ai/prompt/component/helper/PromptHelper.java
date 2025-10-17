package pl.myc22ka.mathapp.ai.prompt.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.modifier.dto.ModifierRequest;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.topic.model.Topic;
import pl.myc22ka.mathapp.modifier.repository.ModifierRepository;
import pl.myc22ka.mathapp.ai.prompt.repository.PromptRepository;
import pl.myc22ka.mathapp.modifier.validator.ModifierExecutor;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.topic.component.helper.TopicHelper;

import java.util.List;

/**
 * Helper class for managing and validating prompts and modifiers.
 * <p>
 * Handles modifier verification, prompt validation, and saving prompt data.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 06.08.2025
 */
@Component
@RequiredArgsConstructor
public class PromptHelper {

    private final TopicHelper topicHelper;
    private final ModifierRepository modifierRepository;
    private final ModifierExecutor modifierExecutor;
    private final ExpressionFactory expressionFactory;
    private final PromptRepository promptRepository;

    /**
     * Verifies if the given math expression satisfies all modifier rules for the given type.
     *
     * @param expression math expression to check
     * @param type       template type of the prompt
     * @param modifiers  list of modifiers to validate
     * @return {@code true} if all modifiers validate successfully; {@code false} otherwise
     */
    public boolean verify(MathExpression expression, TemplatePrefix type, @NotNull List<Modifier> modifiers) {
        for (Modifier modifier : modifiers) {
            if (!modifierExecutor.validate(modifier, type, expression)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies a list of modifier requests with their value given as {@link ContextRecord} for a given prompt type and context.
     *
     * @param modifierRequests list of modifier requests to verify
     * @param contextRecord    context containing the template string and value
     * @param type             prompt type to validate against
     * @return {@code true} if all modifiers are valid; {@code false} otherwise
     */
    public boolean verifyModifierRequestsWithValue(@NotNull List<ModifierRequest> modifierRequests,
                                                   @NotNull ContextRecord contextRecord,
                                                   @NotNull TemplatePrefix type) {
        if (modifierRequests.isEmpty()) {
            return true;
        }

        Topic topic = topicHelper.findTopicByType(type);
        var expression = expressionFactory.parse(contextRecord);

        for (ModifierRequest request : modifierRequests) {
            Modifier modifier = request.toModifier(topic, modifierRepository, topicHelper);

            if (!modifierExecutor.validate(modifier, type, expression)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifies the response of the prompt and sets verified flag.
     *
     * @param prompt           prompt to verify
     * @param parsedExpression parsed math expression returned from AI
     */
    public void verifyPromptResponse(@NotNull Prompt prompt, MathExpression parsedExpression) {
        boolean allVerified = verify(
                parsedExpression,
                prompt.getTopic().getType(),
                prompt.getModifiers()
        );

        prompt.setVerified(allVerified);
    }

    /**
     * Finds modifiers from modifier requests for a given topic.
     *
     * @param modifierRequests list of modifier requests
     * @param topic            topic related to modifiers
     * @return list of created or found {@link Modifier}
     */
    public List<Modifier> findModifiers(List<ModifierRequest> modifierRequests, Topic topic) {
        if (modifierRequests == null || modifierRequests.isEmpty()) {
            return List.of();
        }

        return modifierRequests.stream()
                .map(req -> req.toModifier(topic, modifierRepository, topicHelper))
                .toList();
    }

    /**
     * Saves the given prompt.
     *
     * @param prompt prompt to save
     */
    public void save(Prompt prompt) {
        promptRepository.save(prompt);
    }
}
