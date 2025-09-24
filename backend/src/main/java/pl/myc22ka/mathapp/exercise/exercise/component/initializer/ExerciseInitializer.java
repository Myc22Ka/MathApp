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
import pl.myc22ka.mathapp.exercise.variant.repository.TemplateExerciseVariantRepository;

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
@DependsOn({"templateExerciseInitializer", "templateExerciseVariantInitializer"})
public class ExerciseInitializer {

    private final ExerciseRepository exerciseRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final TemplateExerciseVariantRepository variantRepository;
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
                Exercise exercise = Exercise.builder()
                        .text(record.text())
                        .answer(record.answer())
                        .verified(record.verified())
                        .rating(record.rating() != null ? record.rating() : 0.0)
                        .contextJson(record.context() != null ? objectMapper.writeValueAsString(record.context()) : null)
                        .build();

                if (record.variantExerciseId() != null) {
                    var variant = variantRepository.findById(record.variantExerciseId())
                            .orElseThrow(() -> new IllegalStateException(
                                    "TemplateExerciseVariant not found: " + record.variantExerciseId()
                            ));

                    exercise.setTemplateExerciseVariant(variant);

                } else if (record.templateExerciseId() != null) {
                    var template = templateExerciseRepository.findById(record.templateExerciseId())
                            .orElseThrow(() -> new IllegalStateException(
                                    "TemplateExercise not found: " + record.templateExerciseId()
                            ));

                    exercise.setTemplateExercise(template);

                } else {
                    throw new IllegalStateException("Either templateExerciseId or variantExerciseId must be provided in JSON");
                }

                exercises.add(exercise);
            }

            exerciseRepository.saveAll(exercises);
            System.out.println("[INIT] " + exercises.size() + " exercises saved.");
        }
    }
}