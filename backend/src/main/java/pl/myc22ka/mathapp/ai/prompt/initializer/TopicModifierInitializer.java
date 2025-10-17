package pl.myc22ka.mathapp.ai.prompt.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.modifier.dto.ModifierSeed;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.topic.model.Topic;
import pl.myc22ka.mathapp.modifier.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.modifier.model.modifiers.RequirementModifier;
import pl.myc22ka.mathapp.modifier.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.modifier.repository.ModifierRepository;
import pl.myc22ka.mathapp.topic.repository.TopicRepository;

import java.io.IOException;
import java.util.List;

/**
 * Loads topics and modifiers from JSON files at application startup.
 * Runs only if the database is empty.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
@Component
@RequiredArgsConstructor
public class TopicModifierInitializer {

    private final TopicRepository topicRepository;
    private final ModifierRepository modifierRepository;
    private final ObjectMapper objectMapper;

    /**
     * Initializes topics and modifiers from JSON files if the database is empty.
     *
     * @throws IOException if reading the JSON files fails
     */
    @PostConstruct
    public void init() throws IOException {
        if (topicRepository.count() > 0 || modifierRepository.count() > 0) {
            return;
        }

        var topicsStream = new ClassPathResource("data/static/prompts/topics.json").getInputStream();
        var modifiersStream = new ClassPathResource("data/static/prompts/modifiers.json").getInputStream();

        List<Topic> topics = List.of(objectMapper.readValue(topicsStream, Topic[].class));
        List<ModifierSeed> modifierSeeds = List.of(objectMapper.readValue(modifiersStream, ModifierSeed[].class));

        topicRepository.saveAll(topics);

        for (ModifierSeed seed : modifierSeeds) {
            Topic topic = topicRepository.findById(seed.topicId())
                    .orElseThrow(() -> new IllegalStateException("Topic not found with id: " + seed.topicId()));

            Modifier modifier = getModifier(seed, topic);

            modifierRepository.save(modifier);
        }
    }

    /**
     * Creates a {@link Modifier} entity based on the type specified in the seed.
     *
     * @param seed  the modifier seed containing type, text, and other info
     * @param topic the topic associated with the modifier
     * @return the corresponding Modifier entity
     * @throws IllegalArgumentException if the type is unknown
     */
    @NotNull
    private Modifier getModifier(@NotNull ModifierSeed seed, Topic topic) {
        Modifier modifier;
        switch (seed.modifierType().toUpperCase()) {
            case "DIFFICULTY" -> modifier = new DifficultyModifier(topic, seed.modifierText(), seed.difficultyLevel(), seed.description());
            case "REQUIREMENT" -> modifier = new RequirementModifier(topic, seed.modifierText(), seed.requirement(), seed.description());
            case "TEMPLATE" -> modifier = new TemplateModifier(topic, seed.modifierText(), seed.template(), seed.description());
            default -> throw new IllegalArgumentException("Unknown modifier type: " + seed.modifierType());
        }
        return modifier;
    }
}
