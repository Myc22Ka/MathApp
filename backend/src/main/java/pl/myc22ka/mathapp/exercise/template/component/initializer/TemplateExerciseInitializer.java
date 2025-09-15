package pl.myc22ka.mathapp.exercise.template.component.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.step.model.Step;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Initializes template exercises from JSON on application startup.
 * Automatically creates default steps for each exercise.
 *
 * @author Myc22Ka
 * @version 1.0.6
 * @since 14.09.2025
 */
@Component
@RequiredArgsConstructor
public class TemplateExerciseInitializer {
    private final TemplateExerciseRepository exerciseRepository;
    private final ObjectMapper objectMapper;
    private final TemplateResolver templateResolver;

    @PostConstruct
    public void init() throws IOException {
        System.out.println("[INIT] TemplateExerciseInitializer");

        if (exerciseRepository.count() > 0) {
            System.out.println("[INIT] Template exercises already exist, skipping initialization.");
            return;
        }

        try (InputStream inputStream = new ClassPathResource(
                "data/static/exercises/template-exercises.json"
        ).getInputStream()) {
            TemplateExercise[] exercisesArray = objectMapper.readValue(inputStream, TemplateExercise[].class);
            List<TemplateExercise> exercises = List.of(exercisesArray);

            for (TemplateExercise exercise : exercises) {
                String cleanText = templateResolver.removeTemplatePlaceholders(exercise.getTemplateText());
                exercise.setClearText(cleanText);

                Set<String> prefixes = templateResolver.findTemplatePrefixes(exercise.getTemplateText());
                exercise.setTemplatePrefixes(new ArrayList<>(prefixes));

                exercise.setExerciseCounter(0L);

                if (exercise.getSteps() != null) {
                    for (Step step : exercise.getSteps()) {
                        step.setExercise(exercise);

                        if (step.getStepText() == null || step.getStepText().isEmpty()) {
                            step.setStepText(step.getStepType().getDescription());
                        }

                        if (step.getPrefixes() == null) {
                            step.setPrefixes(new ArrayList<>());
                        }
                    }
                }
            }

            exerciseRepository.saveAll(exercises);
            System.out.println("[INIT] " + exercises.size() + " template exercises saved with steps.");
        }
    }
}
