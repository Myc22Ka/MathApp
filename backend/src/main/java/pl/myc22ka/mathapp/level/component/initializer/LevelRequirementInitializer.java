package pl.myc22ka.mathapp.level.component.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.level.model.LevelRequirement;
import pl.myc22ka.mathapp.level.repository.LevelRequirementRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Initializes level requirements from a JSON file.
 * <p>
 * Loads level XP thresholds into the database on startup
 * if no entries exist in the {@link LevelRequirementRepository}.
 * <p>
 * Example JSON path: src/main/resources/data/static/levels.json
 *
 * @author Myc22Ka
 * @since 27.10.2025
 */
@Component
@RequiredArgsConstructor
public class LevelRequirementInitializer {

    private final LevelRequirementRepository levelRequirementRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws IOException {
        System.out.println("[INIT] LevelRequirementInitializer");

        if (levelRequirementRepository.count() > 0) {
            System.out.println("[INIT] Level requirements already exist, skipping initialization.");
            return;
        }

        try (InputStream inputStream = new ClassPathResource(
                "data/static/user/levels.json"
        ).getInputStream()) {

            LevelRequirement[] levels = objectMapper.readValue(inputStream, LevelRequirement[].class);
            levelRequirementRepository.saveAll(List.of(levels));
            System.out.println("[INIT] " + levels.length + " level requirements saved from JSON.");
        }
    }
}
