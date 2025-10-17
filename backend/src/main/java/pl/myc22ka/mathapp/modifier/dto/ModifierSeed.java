package pl.myc22ka.mathapp.modifier.dto;

import pl.myc22ka.mathapp.modifier.model.modifiers.Requirement;
import pl.myc22ka.mathapp.modifier.model.modifiers.Template;

/**
 * DTO used for seeding modifiers into the database during application initialization.
 *
 * @param modifierType   type of the modifier (DIFFICULTY, REQUIREMENT, TEMPLATE)
 * @param modifierText   human-readable label or description of the modifier
 * @param difficultyLevel difficulty level (only for type = DIFFICULTY)
 * @param requirement    specific requirement constraint (only for type = REQUIREMENT)
 * @param template       template type (only for type = TEMPLATE)
 * @param topicId        ID of the topic this modifier belongs to
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
public record ModifierSeed(
        String modifierType,
        String modifierText,
        Integer difficultyLevel,
        Requirement requirement,
        Template template,
        String description,
        Long topicId
) {
}
