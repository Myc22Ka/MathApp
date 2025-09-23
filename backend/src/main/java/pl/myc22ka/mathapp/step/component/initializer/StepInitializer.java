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

@Component
@RequiredArgsConstructor
public class StepInitializer {

    private final StepDefinitionRepository stepDefinitionRepository;
    private final ObjectMapper objectMapper;

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
