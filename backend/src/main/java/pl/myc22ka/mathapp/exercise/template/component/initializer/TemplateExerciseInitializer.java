package pl.myc22ka.mathapp.exercise.template.component.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.model.StepWrapper;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;

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
@DependsOn("stepInitializer")
public class TemplateExerciseInitializer {

    private final TemplateExerciseRepository exerciseRepository;
    private final StepDefinitionRepository stepDefinitionRepository;
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
                // clear text i prefixy
                String cleanText = templateResolver.removeTemplatePlaceholders(exercise.getTemplateText());
                exercise.setClearText(cleanText);

                Set<String> prefixes = templateResolver.findTemplatePrefixes(exercise.getTemplateText());
                exercise.setTemplatePrefixes(new ArrayList<>(prefixes));

                exercise.setExerciseCounter(0L);

                if (exercise.getSteps() != null) {
                    List<StepWrapper> wrappedSteps = new ArrayList<>();

                    for (StepWrapper step : exercise.getSteps()) {
                        step.setExercise(exercise);

                        // pobieramy StepDefinition po stepDefinitionId z JSON-a
                        Long defId = step.getStepDefinitionId(); // dodaj getter/setter w StepWrapper dla JSON
                        StepDefinition definition = stepDefinitionRepository.findById(defId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "StepDefinition not found with id " + defId));

                        step.setStepDefinition(definition);

                        if (step.getPrefixes() == null) {
                            step.setPrefixes(new ArrayList<>());
                        }

                        wrappedSteps.add(step);
                    }

                    exercise.getSteps().clear();
                    exercise.getSteps().addAll(wrappedSteps);
                }
            }

            exerciseRepository.saveAll(exercises);
            System.out.println("[INIT] " + exercises.size() + " template exercises saved with steps.");
        }
    }
}
