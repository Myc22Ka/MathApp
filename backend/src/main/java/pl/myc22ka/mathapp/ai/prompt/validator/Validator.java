package pl.myc22ka.mathapp.ai.prompt.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.ai.prompt.validator.validators.DifficultyValidator;

@Component
@RequiredArgsConstructor
public class Validator {

    private final DifficultyValidator difficultyValidator;

    public boolean validateDifficulty(DifficultyModifier modifier, PromptType promptType, String response) {
        return difficultyValidator.validate(modifier, promptType, response);
    }

    // Tutaj można dodać inne metody dla różnych typów walidacji
    // public ValidationResult validateTopic(TopicModifier modifier, PromptType promptType) { ... }
    // public ValidationResult validateFormat(FormatModifier modifier, PromptType promptType) { ... }
}
