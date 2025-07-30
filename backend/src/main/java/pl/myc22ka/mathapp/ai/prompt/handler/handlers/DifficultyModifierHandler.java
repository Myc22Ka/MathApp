package pl.myc22ka.mathapp.ai.prompt.handler.handlers;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.handler.ModifierHandler;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.ai.prompt.validator.Validator;

@Component
@RequiredArgsConstructor
public class DifficultyModifierHandler implements ModifierHandler<DifficultyModifier> {

    private final Validator validator;

    @Override
    public boolean supports(Modifier modifier) {
        return modifier instanceof DifficultyModifier;
    }

    @Override
    public boolean apply(DifficultyModifier modifier, PromptType promptType, String response) {
        return validator.validateDifficulty(modifier, promptType, response);
    }
}
