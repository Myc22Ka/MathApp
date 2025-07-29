package pl.myc22ka.mathapp.ai.prompt.dto;

import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Requirement;

public record ModifierSeed(
        String modifierType,
        String modifierText,
        Integer difficultyLevel,
        Requirement requirement,
        Long topicId
) {
}
