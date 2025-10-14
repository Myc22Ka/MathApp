package pl.myc22ka.mathapp.exercise.template.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;

import java.util.*;

/**
 * Helper class for managing TemplateExercise entities.
 * Provides methods for fetching, validating, preparing, and updating templates.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Component
@RequiredArgsConstructor
public class TemplateExerciseHelper {

    private final TemplateExerciseRepository templateExerciseRepository;
    private final TemplateResolver templateResolver;

    /**
     * Retrieves a TemplateExercise by its ID.
     *
     * @param templateId the template ID
     * @return the found TemplateExercise
     * @throws IllegalArgumentException if template not found
     */
    public TemplateExercise getTemplate(Long templateId) {
        return templateExerciseRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with id " + templateId));
    }

    /**
     * Determines if the update is a soft update (minor changes). I keep everything related in database.
     *
     * @param existing the existing template
     * @param updated the updated template
     * @return true if the update is soft, false otherwise
     */
    public boolean isSoftUpdate(@NotNull TemplateExercise existing, @NotNull TemplateExercise updated) {
        String newClearText = updated.getClearText();
        List<String> newPrefixes = updated.getTemplatePrefixes() != null
                ? updated.getTemplatePrefixes()
                : Collections.emptyList();

        return !Objects.equals(existing.getClearText(), newClearText) &&
                Objects.equals(existing.getTemplatePrefixes(), newPrefixes) &&
                Objects.equals(existing.getTemplateAnswer(), updated.getTemplateAnswer()) &&
                Objects.equals(existing.getCategory(), updated.getCategory()) &&
                Objects.equals(existing.getDifficulty(), updated.getDifficulty());
    }

    /**
     * Prepares a TemplateExercise for creation.
     * Sets clear text, prefixes, and resets the exercise counter.
     *
     * @param template the TemplateExercise to prepare
     */
    public void prepareForCreate(@NotNull TemplateExercise template) {
        String cleanText = templateResolver.removeTemplatePlaceholders(template.getTemplateText());
        template.setClearText(cleanText);

        Set<String> newPrefixes = templateResolver.findTemplatePrefixes(template.getTemplateText());
        template.setTemplatePrefixes(new ArrayList<>(newPrefixes));

        template.setExerciseCounter(0L);
    }

    /**
     * Applies a soft update to an existing template.
     * Only updates clear text and template text.
     *
     * @param existing the existing template
     * @param updated the updated template
     */
    public void applySoftUpdate(@NotNull TemplateExercise existing, @NotNull TemplateExercise updated) {
        existing.setClearText(updated.getClearText());
        existing.setTemplateText(updated.getTemplateText());
    }

    /**
     * Applies a hard update to an existing template.
     * Clears steps, exercises, and variants, and updates all relevant fields.
     *
     * @param existing the existing template
     * @param updated the updated template
     */
    public void applyHardUpdate(@NotNull TemplateExercise existing, @NotNull TemplateExercise updated) {
        existing.getSteps().clear();
        existing.getExercises().clear();
        existing.getVariants().clear();

        existing.setCategory(updated.getCategory());
        existing.setDifficulty(updated.getDifficulty());
        existing.setTemplateText(updated.getTemplateText());
        existing.setTemplateAnswer(updated.getTemplateAnswer());
        existing.setClearText(updated.getClearText());
        existing.setTemplatePrefixes(
                updated.getTemplatePrefixes() != null
                        ? new ArrayList<>(updated.getTemplatePrefixes())
                        : new ArrayList<>()
        );

        if (updated.getSteps() != null) {
            updated.getSteps().forEach(step -> step.setExercise(existing));
            existing.getSteps().addAll(updated.getSteps());
        }

        existing.setExerciseCounter(0L);
    }
}
