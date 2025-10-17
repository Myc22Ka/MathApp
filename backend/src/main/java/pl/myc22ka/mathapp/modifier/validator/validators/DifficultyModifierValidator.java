package pl.myc22ka.mathapp.modifier.validator.validators;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.modifier.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.modifier.service.ModifierService;
import pl.myc22ka.mathapp.modifier.validator.ModifierValidator;
import pl.myc22ka.mathapp.exceptions.custom.PromptValidatorException;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.utils.checker.SetChecker;

import static pl.myc22ka.mathapp.model.expression.TemplatePrefix.SET;

/**
 * Validator for DifficultyModifier.
 * <p>
 * Validates difficulty level against max allowed and checks set difficulty rules.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
@Component
@RequiredArgsConstructor
public class DifficultyModifierValidator implements ModifierValidator<DifficultyModifier> {

    private final ModifierService modifierService;
    private final SetChecker setChecker;

    @Override
    public boolean supports(Modifier modifier) {
        return modifier instanceof DifficultyModifier;
    }

    @Override
    public boolean validate(DifficultyModifier modifier, TemplatePrefix type, MathExpression response) {
        if (type == SET && response instanceof ISet set) {
            return validateSets(modifier, set);
        }

        throw new PromptValidatorException("Unsupported Type: " + type);
    }

    @Override
    public boolean validateSets(@NotNull DifficultyModifier modifier, ISet set) {
        int maxLevel = modifierService.getMaxDifficultyLevel(modifier.getTopic());

        if (modifier.getDifficultyLevel() > maxLevel) {
            throw new PromptValidatorException("Difficulty level for this topic is too high");
        }

        return switch (modifier.getDifficultyLevel()) {
            case 1 -> setChecker.checkDifficultyLevel1(set);
            case 2 -> setChecker.checkDifficultyLevel2(set);
            case 3 -> setChecker.checkDifficultyLevel3(set);
            case 4 -> setChecker.checkDifficultyLevel4(set);
            case 5 -> setChecker.checkDifficultyLevel5(set);
            case 6 -> setChecker.checkDifficultyLevel6(set);
            default -> false;
        };
    }
}
