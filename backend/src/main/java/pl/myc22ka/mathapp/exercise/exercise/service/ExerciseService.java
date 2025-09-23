package pl.myc22ka.mathapp.exercise.exercise.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.exercise.exercise.component.helper.ExerciseHelper;
import pl.myc22ka.mathapp.exercise.exercise.component.helper.ValidationHelper;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;
import pl.myc22ka.mathapp.exercise.template.component.helper.TemplateExerciseHelper;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.component.helper.VariantExerciseHelper;

import java.util.*;

/**
 * Service class for managing Exercise entities.
 * Handles creation, generation, update, retrieval, and deletion of exercises.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 13.09.2025
 */
@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final OllamaService ollamaService;
    private final ExerciseHelper exerciseHelper;
    private final TemplateExerciseHelper templateExerciseHelper;
    private final VariantExerciseHelper variantExerciseHelper;
    private final ValidationHelper validationHelper;

    /**
     * Creates a new Exercise from a template or variant with given values.
     *
     * @param templateId the template ID (nullable)
     * @param variantId the variant ID (nullable)
     * @param values the list of values for placeholders
     * @return the saved Exercise entity
     */
    public Exercise create(Long templateId, Long variantId, @NotNull List<String> values) {
        validationHelper.validateTemplateOrVariant(templateId, variantId);

        TemplateExercise template = templateId != null
                ? templateExerciseHelper.getTemplate(templateId)
                : variantExerciseHelper.getVariant(variantId).getTemplateExercise();

        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);
        exerciseHelper.validatePlaceholderCount(placeholders, values);

        List<PrefixValue> context = exerciseHelper.buildContext(placeholders, values);

        boolean allVerified = exerciseHelper.verifyPlaceholders(placeholders, values, context, template.getCategory());
        String finalText = exerciseHelper.resolveText(template, context);

        Exercise exercise = exerciseHelper.buildExercise(template, context, finalText, allVerified);

        return exerciseRepository.save(exercise);
    }

    /**
     * Retrieves an Exercise by its ID.
     */
    public Exercise getById(Long id) {
        return exerciseHelper.getExercise(id);
    }

    /**
     * Retrieves all exercises from the database.
     */
    public List<Exercise> getAll() {
        return exerciseRepository.findAll();
    }

    /**
     * Deletes an Exercise by its ID.
     */
    public void delete(Long id) {
        exerciseRepository.deleteById(id);
    }

    /**
     * Generates a new Exercise using AI prompts for placeholders.
     */
    public Exercise generate(Long templateId, Long variantId) {
        validationHelper.validateTemplateOrVariant(templateId, variantId);

        TemplateExercise template = templateId != null
                ? templateExerciseHelper.getTemplate(templateId)
                : variantExerciseHelper.getVariant(variantId).getTemplateExercise();

        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);

        List<PrefixValue> context = new ArrayList<>();
        boolean allVerified = true;

        for (var entry : placeholders) {
            Prompt prompt = ollamaService.generatePrompt(
                    new MathExpressionChatRequest(
                            PromptType.valueOf(entry.prefix().name()),
                            entry.modifiers() == null ? new ArrayList<>() : entry.modifiers()
                    ).withContext(context)
            );

            String response = prompt.getResponseText();
            context.add(new PrefixValue(entry.prefix().getKey() + entry.index(), response));

            if (!prompt.isVerified()) {
                allVerified = false;
            }
        }

        String finalText = exerciseHelper.resolveText(template, context);

        Exercise exercise = exerciseHelper.buildExercise(template, context, finalText, allVerified);

        return exerciseRepository.save(exercise);
    }

    /**
     * Updates an existing Exercise with new values.
     */
    public Exercise update(Long id, @NotNull List<String> values) {
        Exercise exercise = exerciseHelper.getExercise(id);
        TemplateExercise template = templateExerciseHelper.getTemplate(exercise.getTemplateExercise().getId());
        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);

        exerciseHelper.validatePlaceholderCount(placeholders, values);
        List<PrefixValue> context = exerciseHelper.buildContext(placeholders, values);
        boolean allVerified = exerciseHelper.verifyPlaceholders(placeholders, values, context, template.getCategory());
        String finalText = exerciseHelper.resolveText(template, context);

        exercise.setText(finalText);
        exercise.setVerified(allVerified);
        exercise.setContextJson(exerciseHelper.serializeContext(context));

        String answer = exerciseHelper.calculateAnswer(template, context);
        exercise.setAnswer(answer);

        return exerciseRepository.save(exercise);
    }

    @Transactional
    public Exercise resolve(Long exerciseId) {
        Exercise exercise = exerciseHelper.getExercise(exerciseId);

        List<PrefixValue> context = exerciseHelper.deserializeContext(exercise.getContextJson());

        String answer = exerciseHelper.calculateAnswer(exercise.getTemplateExercise(), context);
        exercise.setAnswer(answer);

        return exerciseRepository.save(exercise);
    }

    public String solve(Long exerciseId) {
        Exercise exercise = exerciseHelper.getExercise(exerciseId);

        return exercise.getAnswer();
    }
}
