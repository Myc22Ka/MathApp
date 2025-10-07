package pl.myc22ka.mathapp.ai.prompt.validator.validators;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.validator.ModifierValidator;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.exceptions.custom.PromptValidatorException;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.model.set.ISet;

import static pl.myc22ka.mathapp.model.expression.TemplatePrefix.SET;


/**
 * Validator for TemplateModifier.
 * <p>
 * Validates set expression based on template and additional info.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
@Component
@RequiredArgsConstructor
public class TemplateModifierValidator implements ModifierValidator<TemplateModifier> {

    @Override
    public boolean supports(Modifier modifier) {
        return modifier instanceof TemplateModifier;
    }

    @Override
    public boolean validate(TemplateModifier modifier, TemplatePrefix type, MathExpression response) {
        if (type == SET && response instanceof ISet set) {
            return validateSets(modifier, set);
        }

        throw new PromptValidatorException("Unsupported Type: " + type);
    }

    private boolean validateSets(@NotNull TemplateModifier modifier, ISet set) {

        if(modifier.getInformation() instanceof ISet other) {

            return switch (modifier.getTemplate()) {
                case DISJOINT_SETS -> set.areDisjoint(other);
            };
        }

        return false;
    }
}