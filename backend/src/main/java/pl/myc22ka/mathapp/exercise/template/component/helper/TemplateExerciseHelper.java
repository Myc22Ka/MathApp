package pl.myc22ka.mathapp.exercise.template.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.exceptions.custom.TemplateAlreadyExistsException;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TemplateExerciseHelper {

    private final TemplateExerciseRepository templateExerciseRepository;
    private final TemplateResolver templateResolver;

    public TemplateExercise getTemplate(Long templateId) {
        return templateExerciseRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with id " + templateId));
    }

    public void validateUnique(@NotNull TemplateExercise template) {
        String cleanText = templateResolver.removeTemplatePlaceholders(template.getTemplateText());
        List<TemplateExercise> allTemplates = templateExerciseRepository.findAll();

        for (TemplateExercise t : allTemplates) {
            if (t.getClearText().equals(cleanText)) {
                throw new TemplateAlreadyExistsException(
                        "Template with the same text already exists"
                );
            }
        }
    }

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

    public void prepareForCreate(@NotNull TemplateExercise template) {
        String cleanText = templateResolver.removeTemplatePlaceholders(template.getTemplateText());
        template.setClearText(cleanText);

        Set<String> newPrefixes = templateResolver.findTemplatePrefixes(template.getTemplateText());
        template.setTemplatePrefixes(new ArrayList<>(newPrefixes));

        template.setExerciseCounter(0L);
    }

    public void applySoftUpdate(@NotNull TemplateExercise existing, @NotNull TemplateExercise updated) {
        existing.setClearText(updated.getClearText());
        existing.setTemplateText(updated.getTemplateText());
    }

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
