package pl.myc22ka.mathapp.ai.prompt.validator.validators;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.ai.prompt.service.ModifierService;
import pl.myc22ka.mathapp.model.set.utils.checker.SetChecker;
import pl.myc22ka.mathapp.exceptions.custom.PromptValidatorException;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.Set;

@Component
@RequiredArgsConstructor
public class DifficultyValidator {

    private final ModifierService modifierService;
    private final SetChecker setChecker;

    public boolean validate(DifficultyModifier modifier, PromptType promptType, String response) {
        if (promptType == PromptType.SET) {
            return validateSets(modifier, response);
        }

        if (promptType == PromptType.FUNCTION) {
            throw new PromptValidatorException("Function is current not supported");
        }

        throw new PromptValidatorException("Unsupported PromptType: " + promptType);
    }

    private boolean validateSets(@NotNull DifficultyModifier modifier, String response) {
        int maxLevel = modifierService.getMaxDifficultyLevel(modifier.getTopic());

        // TODO: When I do Parent Parser I need to remove this line
        ISet set = Set.of(response);

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
