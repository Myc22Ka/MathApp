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
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
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
     * @param templateId the template ID (nullable if variantId provided)
     * @param variantId  the variant ID (nullable if templateId provided)
     * @param values     list of user-provided values for placeholders
     * @return the saved Exercise entity
     * @throws IllegalArgumentException if both or neither templateId/variantId are provided, or validation fails
     */
    @Transactional
    public Exercise create(Long templateId, Long variantId, @NotNull List<String> values) {
        validationHelper.validateTemplateOrVariant(templateId, variantId);

        TemplateLike template = templateId != null
                ? templateExerciseHelper.getTemplate(templateId)
                : variantExerciseHelper.getVariant(variantId);

        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);
        exerciseHelper.validateExercise(placeholders, values);

        List<ContextRecord> context = exerciseHelper.buildContext(placeholders, values);

        boolean allVerified = exerciseHelper.verifyPlaceholders(placeholders, context, template.getCategory());
        String finalText = exerciseHelper.resolveText(template, context);

        Exercise exercise = exerciseHelper.buildExercise(template, context, finalText, allVerified);

        return exerciseRepository.save(exercise);
    }

    /**
     * Retrieves an Exercise by its ID.
     *
     * @param id the exercise ID
     * @return the Exercise entity
     * @throws IllegalStateException if exercise not found
     */
    public Exercise getById(Long id) {
        return exerciseHelper.getExercise(id);
    }

    /**
     * Retrieves paginated and optionally filtered exercises.
     *
     * @param page          zero-based page index
     * @param size          number of items per page
     * @param rating        optional filter by rating
     * @param difficulty    optional filter by difficulty level
     * @param category      optional filter by category
     * @param sortBy        field used for sorting
     * @param sortDirection sort direction ("asc" or "desc")
     * @param templateId    optional filter by template ID
     * @return page of ExerciseDTO matching criteria
     * @throws IllegalArgumentException if rating or difficulty filters are invalid
     */
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

    /**
     * Rates an exercise with a new rating.
     *
     * @param exerciseId the exercise ID
     * @param newRating  the rating value (1–5, step 0.5)
     * @throws IllegalStateException if exercise not found
     */
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
     * Deletes an exercise by its ID.
     *
     * @param id the exercise ID
     */
    public void delete(Long id) {
        exerciseRepository.deleteById(id);
    }

    /**
     * Generates a new Exercise using AI prompts for placeholders.
     *
     * @param templateId the template ID (nullable if variantId provided)
     * @param variantId  the variant ID (nullable if templateId provided)
     * @return the saved Exercise entity
     * @throws IllegalArgumentException if both or neither templateId/variantId are provided
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
     *
     * @param id     the exercise ID
     * @param values list of new values for placeholders
     * @return the updated Exercise entity
     * @throws IllegalStateException if exercise or template not found
     */
    @Transactional
    public Exercise update(Long id, @NotNull List<String> values) {
        Exercise exercise = exerciseHelper.getExercise(id);
        TemplateExercise template = templateExerciseHelper.getTemplate(exercise.getTemplateExercise().getId());
        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);

        exerciseHelper.validateExercise(placeholders, values);
        List<ContextRecord> context = exerciseHelper.buildContext(placeholders, values);
        boolean allVerified = exerciseHelper.verifyPlaceholders(placeholders, context, template.getCategory());
        String finalText = exerciseHelper.resolveText(template, context);

        exercise.setText(finalText);
        exercise.setVerified(allVerified);
        exercise.setContextJson(exerciseHelper.serializeContext(context));

        String answer = exerciseHelper.calculateAnswer(template, context);
        exercise.setAnswer(answer);

        return exerciseRepository.save(exercise);
    }

    /**
     * Checks if the user's answer matches the exercise's correct answer.
     *
     * @param exerciseId the exercise ID
     * @param answer     the user's answer
     * @return true if the answer is correct, false otherwise
     * @throws IllegalStateException if exercise not found
     */
    public boolean solve(Long exerciseId, String answer) {
        Exercise exercise = exerciseHelper.getExercise(exerciseId);

        TemplateLike template = exercise.getTemplateExercise() != null
                ? exercise.getTemplateExercise()
                : exercise.getTemplateExerciseVariant();

        var userAnswer = exerciseHelper.parseValue(new ContextRecord(template.getCategory(), answer));
        var exerciseAnswer = exerciseHelper.parseValue(new ContextRecord(template.getCategory(), exercise.getAnswer()));

        return userAnswer.equals(exerciseAnswer);
    }
}
