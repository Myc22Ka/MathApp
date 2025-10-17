package pl.myc22ka.mathapp.step.component.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Initializes step definitions from a JSON file if the database is empty.
 * <p>
 * This component runs automatically on application startup, loading predefined
 * step definitions to the database to ensure the system has base data for exercises.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 17.10.2025
 */
@Component
@RequiredArgsConstructor
public class StepInitializer {

    private final StepDefinitionRepository stepDefinitionRepository;
    private final ObjectMapper objectMapper;

    /**
     * Initializes step definitions from the JSON file.
     *
     * @throws IOException if reading or parsing the JSON file fails
     */
    @PostConstruct
    public void init() throws IOException {
        System.out.println("[INIT] StepDefinitionInitializer");

        if (stepDefinitionRepository.count() > 0) {
            System.out.println("[INIT] Step definitions already exist, skipping initialization.");
            return;
        }

        try (InputStream inputStream = new ClassPathResource(
                "data/static/exercises/steps.json"
        ).getInputStream()) {

            StepDefinition[] definitionsArray = objectMapper.readValue(inputStream, StepDefinition[].class);
            stepDefinitionRepository.saveAll(List.of(definitionsArray));
            System.out.println("[INIT] " + definitionsArray.length + " step definitions saved from JSON.");
        }
    }
}
