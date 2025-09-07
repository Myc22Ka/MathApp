package pl.myc22ka.mathapp.exercise.template.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.exercise.template.component.helper.TemplateExerciseHelper;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TemplateExerciseService {

    private final TemplateExerciseRepository templateRepository;
    private final TemplateExerciseHelper templateExerciseHelper;

    public void create(@NotNull TemplateExercise template) {
        templateExerciseHelper.validateUnique(template);
        templateExerciseHelper.prepareForCreate(template);

        templateRepository.save(template);
    }

    public List<TemplateExercise> getAll() {
        return templateRepository.findAll();
    }

    public TemplateExercise getById(Long id) {
        return templateExerciseHelper.getTemplate(id);
    }

    @Transactional
    public void update(Long id, @NotNull TemplateExercise updated) {
        TemplateExercise existing = templateExerciseHelper.getTemplate(id);

        if (templateExerciseHelper.isSoftUpdate(existing, updated)) {
            templateExerciseHelper.applySoftUpdate(existing, updated);
        } else {
            templateExerciseHelper.applyHardUpdate(existing, updated);
        }

        templateRepository.save(existing);
    }

    public void delete(Long id) {
        templateRepository.deleteById(id);
    }
}