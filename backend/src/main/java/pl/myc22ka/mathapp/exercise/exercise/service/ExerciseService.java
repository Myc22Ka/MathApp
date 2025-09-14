package pl.myc22ka.mathapp.exercise.exercise.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
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
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.*;

/**
 * Service class for managing Exercise entities.
 * Handles creation, generation, update, retrieval, and deletion of exercises.
 *
 * @author Myc22Ka
 * @version 1.0.0
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

        TemplateExercise template;
        if (templateId != null) {
            template = templateExerciseHelper.getTemplate(templateId);
        } else {
            TemplateExerciseVariant variant = variantExerciseHelper.getVariant(variantId);
            template = variant.getTemplateExercise();
        }

        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);
        exerciseHelper.validatePlaceholderCount(placeholders, values);

        List<PrefixValue> context = exerciseHelper.buildContext(placeholders, values);

        boolean allVerified = exerciseHelper.verifyPlaceholders(placeholders, values, context, template.getCategory());

        String finalText = exerciseHelper.resolveText(template, context);

        Exercise exercise = exerciseHelper.buildExercise(template, values, finalText);
        exercise.setVerified(allVerified);

        return exerciseRepository.save(exercise);
    }
    /**
     * Retrieves an Exercise by its ID.
     *
     * @param id the Exercise ID
     * @return the Exercise entity
     */
    public Exercise getById(Long id) {
        return exerciseHelper.getExercise(id);
    }

    /**
     * Retrieves all exercises from the database.
     *
     * @return list of all Exercise entities
     */
    public List<Exercise> getAll() {
        return exerciseRepository.findAll();
    }

    /**
     * Deletes an Exercise by its ID.
     *
     * @param id the Exercise ID
     */
    public void delete(Long id) {
        exerciseRepository.deleteById(id);
    }

    /**
     * Generates a new Exercise using AI prompts for placeholders.
     *
     * @param templateId the template ID (nullable)
     * @param variantId the variant ID (nullable)
     * @return the generated and saved Exercise entity
     */
    public Exercise generate(Long templateId, Long variantId) {
        validationHelper.validateTemplateOrVariant(templateId, variantId);

        TemplateExercise template;
        if (templateId != null) {
            template = templateExerciseHelper.getTemplate(templateId);
        } else {
            TemplateExerciseVariant variant = variantExerciseHelper.getVariant(variantId);
            template = variant.getTemplateExercise();
        }

        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);

        List<String> values = new ArrayList<>();
        List<PrefixValue> context = new ArrayList<>();

        boolean allVerified = true;

        for (var entry : placeholders) {
            Prompt prompt = ollamaService.generatePrompt(
                    new MathExpressionChatRequest(
                            PromptType.valueOf(entry.prefix().name()),
                            entry.modifiers() == null ? new ArrayList<>() : entry.modifiers()
                    ).withContext(context)
            );

            values.add(prompt.getResponseText());
            context.add(new PrefixValue(entry.prefix().getKey() + entry.index(), prompt.getResponseText()));

            if(!prompt.isVerified()) {
                allVerified = false;
            }
        }

        String finalText = exerciseHelper.resolveText(template, context);
        Exercise exercise = exerciseHelper.buildExercise(template, values, finalText);

        exercise.setVerified(allVerified);

        return exerciseRepository.save(exercise);
    }

    /**
     * Updates an existing Exercise with new values.
     *
     * @param id the Exercise ID
     * @param values the new list of values for placeholders
     * @return the updated Exercise entity
     */
    public Exercise update(Long id, @NotNull List<String> values) {
        Exercise exercise = exerciseHelper.getExercise(id);
        TemplateExercise template = templateExerciseHelper.getTemplate(exercise.getTemplateExercise().getId());
        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);

        exerciseHelper.validatePlaceholderCount(placeholders, values);
        List<PrefixValue> context = exerciseHelper.buildContext(placeholders, values);

        boolean allVerified = exerciseHelper.verifyPlaceholders(placeholders, values, context, template.getCategory());

        String finalText = exerciseHelper.resolveText(template, context);

        exercise.setValues(values);
        exercise.setText(finalText);
        exercise.setVerified(allVerified);

        return exerciseRepository.save(exercise);
    }
}