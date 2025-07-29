package pl.myc22ka.mathapp.ai.prompt.model.modifiers;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;

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

    @Override
    public Modifier findOrCreate(@NotNull ModifierRepository repository) {
        return repository.findByModifierTextAndRequirement(getModifierText(), requirement)
                .orElseGet(() -> repository.save(this));
    }
}
