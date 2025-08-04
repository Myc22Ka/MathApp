package pl.myc22ka.mathapp.ai.prompt.dto;

import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Requirement;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Template;

public record ModifierSeed(
        String modifierType,
        String modifierText,
        Integer difficultyLevel,
        Requirement requirement,
        Template template,
        Long topicId
) {
}
