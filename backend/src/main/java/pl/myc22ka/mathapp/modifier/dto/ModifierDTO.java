package pl.myc22ka.mathapp.modifier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

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

        @Schema(description = "Template code used in placeholders", example = "D1")
        String templateCode,

        @Schema(description = "Type of modifier (class name)", example = "DifficultyModifier")
        String modifierType
) {

    /**
     * Converts a {@link Modifier} entity to a {@link ModifierDTO}.
     *
     * @param modifier the modifier entity to convert
     * @return the corresponding DTO, or {@code null} if input is null
     */
    public static ModifierDTO fromEntity(Modifier modifier) {
        if (modifier == null) {
            return null;
        }

        return new ModifierDTO(
                modifier.getId(),
                modifier.getDescription(),
                modifier.getTopic() != null ? modifier.getTopic().getType() : null,
                modifier.getTemplateCode(),
                modifier.getClass().getSimpleName()
        );
    }

    /**
     * Converts a {@link Page} of {@link Modifier} entities to a {@link Page} of {@link ModifierDTO}.
     *
     * @param modifierPage page of modifier entities
     * @return page of corresponding DTOs
     */
    @NotNull
    public static Page<ModifierDTO> fromPage(@NotNull Page<Modifier> modifierPage) {
        return modifierPage.map(ModifierDTO::fromEntity);
    }
}
