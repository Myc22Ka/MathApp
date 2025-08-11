package pl.myc22ka.mathapp.ai.prompt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierRequest;
import pl.myc22ka.mathapp.ai.prompt.validator.ModifierExecutor;
import pl.myc22ka.mathapp.ai.prompt.validator.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;
import pl.myc22ka.mathapp.ai.prompt.repository.PromptRepository;
import pl.myc22ka.mathapp.ai.prompt.repository.TopicRepository;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.List;
import java.util.Map;

/**
 * Service for managing Prompts and their verification.
 * <p>
 * Handles prompt creation, saving, and response validation.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PromptService {

    private final TopicRepository topicRepository;
    private final ModifierRepository modifierRepository;
    private final PromptRepository promptRepository;
    private final ModifierExecutor modifierExecutor;
    private final TemplateResolver templateResolver;
    private final ExpressionFactory expressionFactory;

    /**
     * Saves the given prompt.
     *
     * @param prompt prompt to save
     */
    public void save(Prompt prompt) {
        promptRepository.save(prompt);
    }

    /**
     * Verifies the response of the prompt and sets verified flag.
     *
     * @param prompt prompt to verify
     */
    public void verifyPromptResponse(@NotNull Prompt prompt) {
        boolean allVerified = verify(
                prompt.getResponseText(),
                prompt.getTopic().getType(),
                prompt.getModifiers()
        );

        prompt.setVerified(allVerified);
    }

    /**
     * Verifies a user's math expression request.
     *
     * @param request math expression request
     * @return true if valid, false otherwise
     */
    public boolean verifyUserMathExpressionRequest(@NotNull MathExpressionRequest request) {
        Topic topic = findTopicByType(request.topicType());
        List<Modifier> modifiers = createOrFindModifiers(request.modifiers(), topic);

        return verify(request.response(), request.topicType(), modifiers);
    }

    /**
     * Creates a prompt based on the chat request.
     *
     * @param request chat request data
     * @return created prompt
     */
    public Prompt createPrompt(@NotNull MathExpressionChatRequest request) {
        Topic topic = findTopicByType(request.topicType());
        List<Modifier> modifiers = createOrFindModifiers(request.modifiers(), topic);

        for (Modifier modifier : modifiers) {
            if (modifier instanceof TemplateModifier t && t.getInformation() != null) {
                String original = t.getModifierText();
                String resolved = templateResolver.resolve(original, Map.of(
                        TemplatePrefix.SET.getKey(), t.getInformation()
                ));
                t.setModifierText(resolved);
            }
        }

        Prompt prompt = Prompt.builder()
                .topic(topic)
                .modifiers(modifiers)
                .build();

        prompt.buildFinalPromptText();

        return prompt;
    }

    private boolean verify(String responseText, PromptType type, @NotNull List<Modifier> modifiers) {
        var expression = expressionFactory.parse(responseText);
        for (Modifier modifier : modifiers) {
            if (!modifierExecutor.validate(modifier, type, expression)) {
                return false;
            }
        }
        return true;
    }

    private Topic findTopicByType(PromptType topicType) {
        return topicRepository.findFirstByType(topicType)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nie znaleziono tematu dla typu: " + topicType));
    }

    private List<Modifier> createOrFindModifiers(List<ModifierRequest> modifierRequests, Topic topic) {
        if (modifierRequests == null || modifierRequests.isEmpty()) {
            return List.of();
        }

        return modifierRequests.stream()
                .map(req -> req.toModifier(topic, modifierRepository))
                .toList();
    }
}