package pl.myc22ka.mathapp.exercise.template.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;

@Service
@RequiredArgsConstructor
public class TemplateExerciseService {

    private final TemplateExerciseRepository repository;

    public TemplateExercise create(TemplateExercise exercise) {
        return repository.save(exercise);
    }
}