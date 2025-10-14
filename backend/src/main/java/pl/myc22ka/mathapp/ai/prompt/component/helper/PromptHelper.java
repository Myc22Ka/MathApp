package pl.myc22ka.mathapp.ai.prompt.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.dto.ContextRecord;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierRequest;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;
import pl.myc22ka.mathapp.ai.prompt.repository.PromptRepository;
import pl.myc22ka.mathapp.ai.prompt.validator.ModifierExecutor;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromptHelper {

    private final TopicHelper topicHelper;
    private final ModifierRepository modifierRepository;
    private final ModifierExecutor modifierExecutor;
    private final ExpressionFactory expressionFactory;
    private final PromptRepository promptRepository;

    public boolean verify(MathExpression expression, TemplatePrefix type, @NotNull List<Modifier> modifiers) {
        for (Modifier modifier : modifiers) {
            if (!modifierExecutor.validate(modifier, type, expression)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies a list of ModifierRequest values for a given prompt type.
     *
     * @param modifierRequests list of modifier requests to verify
     * @param value            corresponding value to verify
     * @param type             prompt type
     * @return true if all values pass validation, false otherwise
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
     * @param prompt prompt to verify
     */
    public void verifyPromptResponse(@NotNull Prompt prompt, MathExpression parsedExpression) {
        boolean allVerified = verify(
                parsedExpression,
                prompt.getTopic().getType(),
                prompt.getModifiers()
        );

        prompt.setVerified(allVerified);
    }

    public List<Modifier> createOrFindModifiers(List<ModifierRequest> modifierRequests, Topic topic) {
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
