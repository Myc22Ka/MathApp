package pl.myc22ka.mathapp.ai.prompt.model.modifiers;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;

/**
 * Difficulty modifier entity.
 * <p>
 * Represents difficulty level for a given topic.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
@Entity
@DiscriminatorValue("DIFFICULTY")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DifficultyModifier extends Modifier {

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    /**
     * Creates a DifficultyModifier instance.
     *
     * @param topic           the associated topic
     * @param modifierText    text describing the modifier
     * @param difficultyLevel numeric difficulty level
     */
    public DifficultyModifier(Topic topic, String modifierText, Integer difficultyLevel) {
        super(null, modifierText, topic, "D" + difficultyLevel);
        this.difficultyLevel = difficultyLevel;
    }
}

