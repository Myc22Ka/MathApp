package pl.myc22ka.mathapp.ai.prompt.model.modifiers;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;

/**
 * Requirement modifier entity.
 * <p>
 * Defines a specific requirement for a topic.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Entity
@DiscriminatorValue("REQUIREMENT")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RequirementModifier extends Modifier {

    @Enumerated(EnumType.STRING)
    @Column(name = "requirement")
    private Requirement requirement;

    /**
     * Creates a RequirementModifier instance.
     *
     * @param topic        the associated topic
     * @param modifierText text describing the modifier
     * @param requirement  requirement type
     */
    public RequirementModifier(Topic topic, String modifierText, Requirement requirement) {
        super(null, modifierText, topic);
        this.requirement = requirement;
    }
}
