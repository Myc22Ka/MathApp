package pl.myc22ka.mathapp.exercise.exercise.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.dto.ContextRecord;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.exercise.exercise.component.filter.ExerciseSpecification;
import pl.myc22ka.mathapp.exercise.exercise.component.helper.ExerciseHelper;
import pl.myc22ka.mathapp.exercise.exercise.component.helper.ValidationHelper;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseDTO;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;
import pl.myc22ka.mathapp.exercise.template.component.TemplateLike;
import pl.myc22ka.mathapp.exercise.template.component.helper.TemplateExerciseHelper;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.component.helper.VariantExerciseHelper;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing Exercise entities.
 * Handles creation, generation, update, retrieval, and deletion of exercises.
 *
 * @author Myc22Ka
 * @version 1.0.7
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
     * @param variantId  the variant ID (nullable)
     * @param values     the list of values for placeholders
     * @return the saved Exercise entity
     */
    @Transactional
    public Exercise create(Long templateId, Long variantId, @NotNull List<String> values) {
        validationHelper.validateTemplateOrVariant(templateId, variantId);

        TemplateLike template = templateId != null
                ? templateExerciseHelper.getTemplate(templateId)
                : variantExerciseHelper.getVariant(variantId);

        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);
        exerciseHelper.validatePlaceholderCount(placeholders, values);

        List<ContextRecord> context = exerciseHelper.buildContext(placeholders, values);

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

    public Page<ExerciseDTO> getAll(int page, int size, Double rating, String difficulty,
                                    TemplatePrefix category, String sortBy, @NotNull String sortDirection, Long templateId) {

        validationHelper.validateFilters(rating, difficulty);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Exercise> spec = ExerciseSpecification.withFilters(
                rating, difficulty, category, templateId
        );

        Page<Exercise> exercises = exerciseRepository.findAll(spec, pageable);

        return exercises.map(ExerciseDTO::fromEntity);
    }

    @Transactional
    public void rateExercise(Long exerciseId, Double newRating) {
        Exercise exercise = exerciseHelper.getExercise(exerciseId);

        Double currentRating = exercise.getRating();
        if (currentRating == null) {
            exercise.setRating(newRating);
            exerciseRepository.save(exercise);

            return;
        }

        // TODO: I need to change this formula when I got User in this system
        double average = (currentRating + newRating) / 2.0;
        exercise.setRating(average);

        exerciseRepository.save(exercise);
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
    @Transactional
    public Exercise generate(Long templateId, Long variantId) {
        validationHelper.validateTemplateOrVariant(templateId, variantId);

        TemplateLike template = templateId != null
                ? templateExerciseHelper.getTemplate(templateId)
                : variantExerciseHelper.getVariant(variantId);

        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);

        List<ContextRecord> context = new ArrayList<>();
        boolean allVerified = true;

        for (var entry : placeholders) {
            Prompt prompt = ollamaService.generatePrompt(
                    new MathExpressionChatRequest(
                            entry.prefix(),
                            entry.modifiers() == null ? new ArrayList<>() : entry.modifiers()
                    ).withContext(context)
            );

            String response = prompt.getResponseText();

            context.add(exerciseHelper.buildContextRecord(entry, response));

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
    @Transactional
    public Exercise update(Long id, @NotNull List<String> values) {
        Exercise exercise = exerciseHelper.getExercise(id);
        TemplateExercise template = templateExerciseHelper.getTemplate(exercise.getTemplateExercise().getId());
        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);

        exerciseHelper.validatePlaceholderCount(placeholders, values);
        List<ContextRecord> context = exerciseHelper.buildContext(placeholders, values);
        boolean allVerified = exerciseHelper.verifyPlaceholders(placeholders, values, context, template.getCategory());
        String finalText = exerciseHelper.resolveText(template, context);

        exercise.setText(finalText);
        exercise.setVerified(allVerified);
        exercise.setContextJson(exerciseHelper.serializeContext(context));

        String answer = exerciseHelper.calculateAnswer(template, context);
        exercise.setAnswer(answer);

        return exerciseRepository.save(exercise);
    }

    /**
     * Checks if answer given by user is the same as exercise answer
     */
    public boolean solve(Long exerciseId, String answer) {
        Exercise exercise = exerciseHelper.getExercise(exerciseId);

        var userAnswer = exerciseHelper.parseValue(answer);
        var exerciseAnswer = exerciseHelper.parseValue(exercise.getAnswer());

        return userAnswer.equals(exerciseAnswer);
    }
}
