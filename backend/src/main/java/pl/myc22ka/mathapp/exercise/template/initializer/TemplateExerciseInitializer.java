package pl.myc22ka.mathapp.exercise.template.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;

import java.io.IOException;
import java.util.List;

/**
 * Loads template exercises from a JSON file at application startup.
 * Runs only if the database is empty.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 12.08.2025
 */
@Component
@RequiredArgsConstructor
public class TemplateExerciseInitializer {

    private final TemplateExerciseRepository exerciseRepository;
    private final ObjectMapper objectMapper;

    /**
     * Initializes template exercises from a JSON file if the database is empty.
     *
     * @throws IOException if reading the JSON file fails
     */
    @PostConstruct
    public void init() throws IOException {
        if (exerciseRepository.count() > 0) {
            return;
        }

        var inputStream = new ClassPathResource("data/static/exercises/template-exercises.json").getInputStream();
        List<TemplateExercise> exercises =
                List.of(objectMapper.readValue(inputStream, TemplateExercise[].class));

        for (TemplateExercise exercise : exercises) {
            if (exercise.getSteps() != null) {
                exercise.getSteps().forEach(step -> step.setExercise(exercise));
            }
        }

        exerciseRepository.saveAll(exercises);
    }
}
