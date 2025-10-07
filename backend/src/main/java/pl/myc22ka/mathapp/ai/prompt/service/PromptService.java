package pl.myc22ka.mathapp.ai.prompt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.ai.prompt.component.helper.PromptHelper;
import pl.myc22ka.mathapp.ai.prompt.component.helper.TopicHelper;
import pl.myc22ka.mathapp.ai.prompt.dto.*;
import pl.myc22ka.mathapp.ai.prompt.validator.ModifierExecutor;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;
import pl.myc22ka.mathapp.ai.prompt.repository.PromptRepository;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Service for managing Prompts and their verification.
 * <p>
 * Handles prompt creation, saving, and response validation.
 *
 * @author Myc22Ka
 * @version 1.0.4
 * @since 11.08.2025
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PromptService {

    private final TopicHelper topicHelper;
    private final PromptHelper promptHelper;
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
    public void verifyPromptResponse(@NotNull Prompt prompt, MathExpression parsedExpression) {
        boolean allVerified = promptHelper.verify(
                parsedExpression,
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
        Topic topic = topicHelper.findTopicByType(request.topicType());
        List<Modifier> modifiers = createOrFindModifiers(request.modifiers(), topic);

        var parsed = expressionFactory.parse(request.response());

        return promptHelper.verify(parsed, request.topicType(), modifiers);
    }

    /**
     * Creates a prompt based on the chat request.
     *
     * @param request chat request data
     * @return created prompt
     */
    public Prompt createPrompt(@NotNull MathExpressionChatRequest request) {
        Topic topic = topicHelper.findTopicByType(request.topicType());
        List<Modifier> modifiers = createOrFindModifiers(request.modifiers(), topic);

        List<PrefixValue> context = new ArrayList<>();
        List<Modifier> promptModifiers = new ArrayList<>();

        for (Modifier modifier : modifiers) {
            if (modifier instanceof TemplateModifier tmOriginal && tmOriginal.getModifierText() != null) {

                TemplateModifier t = new TemplateModifier(tmOriginal);

                String templateText = t.getModifierText();

                Set<PrefixModifierEntry> entries = new LinkedHashSet<>(templateResolver.findPrefixModifiers(templateText));

                for (var entry : entries) {
                    context.add(new PrefixValue(
                            entry.prefix().getKey() + entry.index(),
                            t.getInformation() != null ? t.getInformation().toString() : "X"
                    ));
                }

                String resolvedText = templateResolver.replaceTemplatePlaceholders(templateText, context);
                t.setModifierText(resolvedText);

                promptModifiers.add(t);
            } else {
                promptModifiers.add(modifier);
            }
        }

        Prompt prompt = Prompt.builder()
                .topic(topic)
                .modifiers(promptModifiers)
                .build();

        prompt.buildFinalPromptText();

        return prompt;
    }

    public List<Modifier> createOrFindModifiers(List<ModifierRequest> modifierRequests, Topic topic) {
        if (modifierRequests == null || modifierRequests.isEmpty()) {
            return List.of();
        }

        return modifierRequests.stream()
                .map(req -> req.toModifier(topic, modifierRepository))
                .toList();
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
                                                   @NotNull String value,
                                                   @NotNull TemplatePrefix type) {
        if (modifierRequests.isEmpty()) {
            return true;
        }

        Topic topic = topicHelper.findTopicByType(type);
        var expression = expressionFactory.parse(value);

        for (ModifierRequest request : modifierRequests) {
            Modifier modifier = request.toModifier(topic, modifierRepository);

            if (!modifierExecutor.validate(modifier, type, expression)) {
                return false;
            }
        }

        return true;
    }
}