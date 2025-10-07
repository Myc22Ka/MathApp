package pl.myc22ka.mathapp.ai.prompt.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

public record TopicDTO(Long id, TemplatePrefix type, String description) {

    @NotNull
    public static TopicDTO fromEntity(@NotNull Topic topic) {
        return new TopicDTO(topic.getId(), topic.getType(), topic.getDescription());
    }
}