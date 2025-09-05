package pl.myc22ka.mathapp.exercise.template.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.exceptions.custom.TemplateAlreadyExistsException;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TemplateExerciseService {

    private final TemplateExerciseRepository templateRepository;
    private final TemplateResolver templateResolver;

    public TemplateExercise create(@NotNull TemplateExercise exercise) {
        // Usuń template strings z kreacji
        String cleanText = templateResolver.removeTemplatePlaceholders(exercise.getTemplateText());

        // Pobierz wszystkie template z bazy
        List<TemplateExercise> allTemplates = templateRepository.findAll();

        for (TemplateExercise t : allTemplates) {
            String tClean = templateResolver.removeTemplatePlaceholders(t.getTemplateText());

            if (tClean.equals(cleanText)) {
                // Porównujemy templatePrefixes
                Set<String> existingPrefixes = templateResolver.findTemplatePrefixes(t.getTemplateText());
                Set<String> newPrefixes = templateResolver.findTemplatePrefixes(exercise.getTemplateText());

                if (existingPrefixes.equals(newPrefixes)) {
                    // Tekst i prefixy są takie same → traktujemy jako duplikat
                    throw new TemplateAlreadyExistsException(
                            "Template with the same text and template prefixes already exists"
                    );
                }
                // Tekst ten sam, ale prefixy różne → można zapisać nowy template
            }
        }

        // Jeśli nie znaleziono duplikatu → zapisujemy
        return templateRepository.save(exercise);
    }

    public List<TemplateExercise> getAll() {
        return templateRepository.findAll();
    }

    public TemplateExercise getById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TemplateExercise not found with id " + id));
    }

    public TemplateExercise update(Long id, @NotNull TemplateExercise updated) {
        TemplateExercise existing = getById(id);
        existing.setCategory(updated.getCategory());
        existing.setDifficulty(updated.getDifficulty());
        existing.setTemplateText(updated.getTemplateText());
        existing.setTemplateAnswer(updated.getTemplateAnswer());

        existing.getSteps().clear();
        if (updated.getSteps() != null) {
            updated.getSteps().forEach(step -> step.setExercise(existing));
            existing.getSteps().addAll(updated.getSteps());
        }

        return templateRepository.save(existing);
    }

    public void delete(Long id) {
        TemplateExercise existing = getById(id);
        templateRepository.delete(existing);
    }


}