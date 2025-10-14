package pl.myc22ka.mathapp.ai.prompt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.component.helper.PromptHelper;
import pl.myc22ka.mathapp.ai.prompt.component.helper.TopicHelper;
import pl.myc22ka.mathapp.ai.prompt.dto.*;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.exercise.exercise.component.helper.ExerciseHelper;

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
 * @version 1.0.6
 * @since 11.08.2025
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PromptService {

    private final TopicHelper topicHelper;
    private final PromptHelper promptHelper;
    private final TemplateResolver templateResolver;
    private final ExerciseHelper exerciseHelper;

    /**
     * Creates a prompt based on the chat request.
     *
     * @param request chat request data
     * @return created prompt
     */
    public Prompt createPrompt(@NotNull MathExpressionChatRequest request) {
        Topic topic = topicHelper.findTopicByType(request.topicType());
        List<Modifier> modifiers = promptHelper.createOrFindModifiers(request.modifiers(), topic);

        List<ContextRecord> context = new ArrayList<>();
        List<Modifier> promptModifiers = new ArrayList<>();

        for (Modifier modifier : modifiers) {
            if (modifier instanceof TemplateModifier tmOriginal && tmOriginal.getModifierText() != null) {

                TemplateModifier t = new TemplateModifier(tmOriginal);

                String templateText = t.getModifierText();

                Set<PrefixModifierEntry> entries = new LinkedHashSet<>(templateResolver.findPrefixModifiers(templateText));

                for (var entry : entries) {
                    context.add(exerciseHelper.buildContextRecord(entry, t.getInformation().toString()));
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
}