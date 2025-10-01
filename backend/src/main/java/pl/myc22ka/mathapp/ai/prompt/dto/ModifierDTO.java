package pl.myc22ka.mathapp.ai.prompt.dto;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;

public record ModifierDTO(
        Long id,
        String description,
        PromptType topicName,
        String templateCode,
        String modifierType
) {

    /**
     * Tworzy DTO z encji Modifier
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
     * Konwertuje Page<Modifier> na Page<ModifierDTO>
     */
    @NotNull
    public static Page<ModifierDTO> fromPage(@NotNull Page<Modifier> modifierPage) {
        return modifierPage.map(ModifierDTO::fromEntity);
    }
}
