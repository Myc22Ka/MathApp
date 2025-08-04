package pl.myc22ka.mathapp.ai.prompt.model.modifiers;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.model.expression.MathExpression;

@Entity
@DiscriminatorValue("TEMPLATE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TemplateModifier extends Modifier {

    @Enumerated(EnumType.STRING)
    @Column(name = "requirement")
    private Requirement requirement;

    @Transient
    private MathExpression information;

    public TemplateModifier(Topic topic, String modifierText, Requirement requirement) {
        super(null, modifierText, topic);
        this.requirement = requirement;
    }
}
