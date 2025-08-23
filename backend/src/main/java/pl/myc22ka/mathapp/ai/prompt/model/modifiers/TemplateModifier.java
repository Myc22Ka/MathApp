package pl.myc22ka.mathapp.ai.prompt.model.modifiers;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.model.expression.MathExpression;

/**
 * Template modifier entity.
 * <p>
 * Represents a modifier based on a user-provided additional information.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
@Entity
@DiscriminatorValue("TEMPLATE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TemplateModifier extends Modifier {

    @Enumerated(EnumType.STRING)
    @Column(name = "template")
    private Template template;

    @Transient
    private MathExpression information;

    /**
     * Creates a TemplateModifier instance.
     *
     * @param topic        the associated topic
     * @param modifierText text describing the modifier
     * @param template     the template type
     */
    public TemplateModifier(Topic topic, String modifierText, @NotNull Template template) {
        super(null, modifierText, topic, "T" + template.getCode());
        this.template = template;
    }
}
