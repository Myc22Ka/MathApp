package pl.myc22ka.mathapp.exercise.exercise.component.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseInitializerDTO;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Initializes exercises from a static JSON file on application startup.
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 13.09.2025
 */
@Component
@RequiredArgsConstructor
@DependsOn("templateExerciseInitializer")
public class ExerciseInitializer {

    private final ExerciseRepository exerciseRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws IOException {
        System.out.println("[INIT] ExerciseInitializer");

        if (exerciseRepository.count() > 0) {
            System.out.println("[INIT] Exercises already exist, skipping initialization.");
            return;
        }

        try (InputStream inputStream = new ClassPathResource(
                "data/static/exercises/exercises.json"
        ).getInputStream()) {
            ExerciseInitializerDTO[] records = objectMapper.readValue(inputStream, ExerciseInitializerDTO[].class);
            List<Exercise> exercises = new ArrayList<>();

            for (ExerciseInitializerDTO record : records) {
                TemplateExercise template = templateExerciseRepository
                        .findById(record.templateExerciseId())
                        .orElseThrow(() -> new IllegalStateException(
                                "TemplateExercise not found: " + record.templateExerciseId()
                        ));

                exercises.add(ExerciseInitializerDTO.fromRecord(record, template, objectMapper));
            }

            exerciseRepository.saveAll(exercises);
            System.out.println("[INIT] " + exercises.size() + " exercises saved.");
        }
    }
}