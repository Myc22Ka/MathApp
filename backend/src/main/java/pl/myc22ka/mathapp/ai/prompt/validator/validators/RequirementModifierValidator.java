package pl.myc22ka.mathapp.ai.prompt.validator.validators;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.validator.ModifierValidator;
import pl.myc22ka.mathapp.ai.prompt.validator.UtilChecker;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.RequirementModifier;
import pl.myc22ka.mathapp.exceptions.custom.PromptValidatorException;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.utils.checker.SetChecker;

import static pl.myc22ka.mathapp.ai.prompt.model.PromptType.SET;
import static pl.myc22ka.mathapp.model.set.ISetType.FINITE;
import static pl.myc22ka.mathapp.model.set.ISetType.INTERVAL;

/**
 * Validator for RequirementModifier.
 * <p>
 * Checks set constraints based on requirement enum.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Component
@RequiredArgsConstructor
public class RequirementModifierValidator implements ModifierValidator<RequirementModifier> {

    private final SetChecker setChecker;

    @Override
    public boolean supports(Modifier modifier) {
        return modifier instanceof RequirementModifier;
    }

    @Override
    public boolean validate(RequirementModifier modifier, PromptType promptType, MathExpression response) {
        if (promptType == SET && response instanceof ISet set) {
            return validateSets(modifier, set);
        }

        throw new PromptValidatorException("Unsupported PromptType: " + promptType);
    }

    private boolean validateSets(@NotNull RequirementModifier modifier, ISet set) {
        return switch (modifier.getRequirement()) {
            case INTERVALS_ONLY -> set.getISetType() == INTERVAL &&
                    !set.toString().contains("{") &&
                    !set.toString().contains("}");

            case FINITE_SETS_ONLY -> set.getISetType() == FINITE;
            case POSITIVE_ONLY -> set.onlyPositiveElements();
            case DISJOINT_SETS -> set.isDisjoint();
        };
    }
}