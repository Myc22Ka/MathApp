package pl.myc22ka.mathapp.topic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.topic.model.Topic;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Data Transfer Object representing a Topic.
 * <p>
 * Used to send topic information via API, typically for template generation or UI display.
 *
 * @param id          unique identifier of the topic
 * @param type        template prefix associated with the topic
 * @param description human-readable description of the topic
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Schema(description = "Represents a topic used in math templates",
        example = """
                {
                  "id": 1,
                  "type": "SET",
                  "description": "Sets and their properties"
                }
                """)
public record TopicDTO(
        @Schema(description = "Unique ID of the topic", example = "1")
        Long id,

        @Schema(description = "Template prefix/type of the topic", example = "SET")
        TemplatePrefix type,

        @Schema(description = "Human-readable description of the topic", example = "Sets and their properties")
        String description
) {

    /**
     * Converts a {@link Topic} entity to a {@link TopicDTO}.
     *
     * @param topic the topic entity
     * @return the corresponding TopicDTO
     */
    @NotNull
    public static TopicDTO fromEntity(@NotNull Topic topic) {
        return new TopicDTO(topic.getId(), topic.getType(), topic.getDescription());
    }
}