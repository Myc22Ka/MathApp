package pl.myc22ka.mathapp.ai.prompt.model.modifiers;

import jakarta.persistence.*;
import lombok.*;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;

@Entity
@DiscriminatorValue("REQUIREMENT")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RequirementModifier extends Modifier {

    @Enumerated(EnumType.STRING)
    @Column(name = "requirement")
    private Requirement requirement;

    public RequirementModifier(Topic topic, String modifierText, Requirement requirement) {
        super(null, modifierText, topic);
        this.requirement = requirement;
    }
}
