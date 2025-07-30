package pl.myc22ka.mathapp.ai.prompt.model.modifiers;

import jakarta.persistence.*;
import lombok.*;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;

@Entity
@DiscriminatorValue("DIFFICULTY")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DifficultyModifier extends Modifier {

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    public DifficultyModifier(Topic topic, String modifierText, Integer difficultyLevel) {
        super(null, modifierText, topic);
        this.difficultyLevel = difficultyLevel;
    }
}

