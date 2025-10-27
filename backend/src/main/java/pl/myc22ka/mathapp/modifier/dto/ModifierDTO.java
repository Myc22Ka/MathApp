package pl.myc22ka.mathapp.modifier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.modifier.model.ModifierPrefix;
import pl.myc22ka.mathapp.modifier.model.modifiers.*;

/**
 * Data Transfer Object representing a Modifier.
 * <p>
 * Used for returning modifier information via API.
 *
 * @param id           unique identifier of the modifier
 * @param description  human-readable description
 * @param topicName    template prefix / topic associated with the modifier
 * @param templateCode code used in template placeholders
 * @param modifierType type of modifier (class name)
 *
 * @version 1.0.0
 * @author Myc22Ka
 * @since 17.10.2025
 */
@Schema(description = "Represents a template modifier with its metadata")
public record ModifierDTO(
        @Schema(description = "Unique ID of the modifier", example = "12")
        Long id,

        @Schema(description = "Human-readable description of the modifier", example = "Set must be disjoint")
        String description,

        @Schema(description = "Associated topic/template prefix", example = "S")
        TemplatePrefix topicName,

        @Schema(description = "Template code used in placeholders", example = "R4")
        String templateCode,

        @Schema(description = "Type of modifier", example = "REQUIREMENT")
        ModifierPrefix modifierType,

        @Schema(description = "Difficulty level (for type = DIFFICULTY)", example = "1")
        Integer difficultyLevel,

        @Schema(description = "Requirement constraint (for type = REQUIREMENT)", example = "DISJOINT_SETS")
        Requirement requirement,

        @Schema(description = "Template type (for type = TEMPLATE)", example = "UNION")
        Template template,

        @Schema(description = "Optional extra information for TEMPLATE type", example = "(1,4)")
        String templateInformation
) {

    /**
     * Converts a {@link Modifier} entity to a {@link ModifierDTO}.
     */
    public static ModifierDTO fromEntity(Modifier modifier) {
        if (modifier == null) {
            return null;
        }

        Long id = modifier.getId();
        String description = modifier.getDescription();
        TemplatePrefix topicName = modifier.getTopic() != null ? modifier.getTopic().getType() : null;
        String templateCode = modifier.getTemplateCode();
        ModifierPrefix modifierType = ModifierPrefix.fromTemplateCode(templateCode);

        Integer difficultyLevel = null;
        Requirement requirement = null;
        Template template = null;
        String templateInformation = null;

        switch (modifier) {
            case DifficultyModifier diff -> difficultyLevel = diff.getDifficultyLevel();
            case RequirementModifier req -> requirement = req.getRequirement();
            case TemplateModifier tmpl -> template = tmpl.getTemplate();
            default -> {
            }
        }

        return new ModifierDTO(
                id,
                description,
                topicName,
                templateCode,
                modifierType,
                difficultyLevel,
                requirement,
                template,
                templateInformation
        );
    }

    /**
     * Converts a {@link Page} of {@link Modifier} entities to a {@link Page} of {@link ModifierDTO}.
     */
    @NotNull
    public static Page<ModifierDTO> fromPage(@NotNull Page<Modifier> modifierPage) {
        return modifierPage.map(ModifierDTO::fromEntity);
    }
}
