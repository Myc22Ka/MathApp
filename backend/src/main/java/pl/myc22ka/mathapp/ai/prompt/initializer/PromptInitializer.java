package pl.myc22ka.mathapp.ai.prompt.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierSeed;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.RequirementModifier;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;
import pl.myc22ka.mathapp.ai.prompt.repository.TopicRepository;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PromptInitializer {

    private final TopicRepository topicRepository;
    private final ModifierRepository modifierRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws IOException {
        if (topicRepository.count() > 0 || modifierRepository.count() > 0) {
            return;
        }

        // Wczytaj pliki JSON z classpath
        var topicsStream = new ClassPathResource("data/static/prompts/topics.json").getInputStream();
        var modifiersStream = new ClassPathResource("data/static/prompts/modifiers.json").getInputStream();

        // Zmapuj dane
        List<Topic> topics = List.of(objectMapper.readValue(topicsStream, Topic[].class));
        List<ModifierSeed> modifierSeeds = List.of(objectMapper.readValue(modifiersStream, ModifierSeed[].class));

        // Zapisz tematy
        topicRepository.saveAll(topics);

        // Przypnij modyfikatory do tematów
        for (ModifierSeed seed : modifierSeeds) {
            Topic topic = topicRepository.findById(seed.topicId())
                    .orElseThrow(() -> new IllegalStateException("Topic not found with id: " + seed.topicId()));

            Modifier modifier;
            switch (seed.modifierType().toUpperCase()) {
                case "DIFFICULTY" -> modifier = new DifficultyModifier(topic, seed.modifierText(), seed.difficultyLevel());
                case "REQUIREMENT" -> modifier = new RequirementModifier(topic, seed.modifierText(), seed.requirement());
                default -> throw new IllegalArgumentException("Unknown modifier type: " + seed.modifierType());
            }

            modifierRepository.save(modifier);
        }
    }
}
