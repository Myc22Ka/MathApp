package pl.myc22ka.mathapp.exercise.exercise.configuration.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseInitializerDTO;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Initializes exercises from a static JSON file on application startup.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Configuration
@RequiredArgsConstructor
public class ExerciseInitializer {

    private final ExerciseRepository exerciseRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final ObjectMapper objectMapper;

    /**
     * Loads exercises from "exercises.json" and saves them to the database.
     * Runs as a CommandLineRunner with order 3.
     *
     * @return the CommandLineRunner bean
     */
    @Bean
    @Order(3)
    public CommandLineRunner initExercises() {
        return args -> {
            System.out.println("[INIT] ExerciseInitializer");

            if (exerciseRepository.count() == 0) {
                try (InputStream is = getClass().getClassLoader()
                        .getResourceAsStream("data/static/exercises/exercises.json")) {

                    if (is == null) {
                        throw new IllegalArgumentException("Could not find static/exercises/exercises.json in classpath.");
                    }

                    ExerciseInitializerDTO[] records = objectMapper.readValue(is, ExerciseInitializerDTO[].class);
                    List<Exercise> exercises = new ArrayList<>();

                    for (ExerciseInitializerDTO record : records) {
                        TemplateExercise template = templateExerciseRepository
                                .findById(record.templateExerciseId())
                                .orElseThrow(() -> new IllegalStateException(
                                        "TemplateExercise not found: " + record.templateExerciseId()
                                ));

                        exercises.add(ExerciseInitializerDTO.fromRecord(record, template));
                    }

                    exerciseRepository.saveAll(exercises);
                    System.out.println("[INIT] " + exercises.size() + " exercises saved.");
                }
            } else {
                System.out.println("[INIT] Exercises already exist, skipping initialization.");
            }
        };
    }
}