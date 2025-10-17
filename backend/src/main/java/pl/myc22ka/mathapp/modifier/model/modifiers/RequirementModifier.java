package pl.myc22ka.mathapp.modifier.model.modifiers;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.topic.model.Topic;

/**
 * Requirement modifier entity.
 * <p>
 * Defines a specific requirement for a topic.
 *
 * @author Myc22Ka
 * @version 1.0.1
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
    public RequirementModifier(Topic topic, String modifierText, @NotNull Requirement requirement, String description) {
        super(null, modifierText, topic, "R" + requirement.getCode(), description);
        this.requirement = requirement;
    }
}
