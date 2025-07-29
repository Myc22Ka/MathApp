package pl.myc22ka.mathapp.ai.prompt.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Requirement;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.RequirementModifier;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;

public record ModifierRequest(
        String type,
        Integer difficultyLevel,
        Requirement requirement
) {

    public Modifier toModifier(@NotNull Topic topic, @NotNull ModifierRepository repository) {
        return switch (type.toUpperCase()) {
            case "DIFFICULTY" -> repository
                    .findByTopicAndDifficultyLevel(topic, difficultyLevel)
                    .orElseThrow(() -> new IllegalStateException("No DifficultyModifier found for topic=" + topic.getId() + " and level=" + difficultyLevel));
            case "REQUIREMENT" -> repository
                    .findByTopicAndRequirement(topic, requirement)
                    .orElseThrow(() -> new IllegalStateException("No RequirementModifier found for topic=" + topic.getId() + " and requirement=" + requirement));
            default -> throw new IllegalArgumentException("Unknown modifier type: " + type);
        };
    }
}
