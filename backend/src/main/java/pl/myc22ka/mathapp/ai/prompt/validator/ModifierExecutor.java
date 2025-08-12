package pl.myc22ka.mathapp.ai.prompt.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.model.expression.MathExpression;

import java.util.List;

/**
 * Executes validation for given Modifier using matching Validator.
 * <p>
 * Delegates to first supporting ModifierValidator from injected handlers.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Component
@RequiredArgsConstructor
public class ModifierExecutor {

    private final List<ModifierValidator<? extends Modifier>> handlers;

    /**
     * Validate modifier with response for prompt type.
     *
     * @param modifier   the modifier to validate
     * @param promptType the prompt type context
     * @param response   the user's response expression
     * @return true if valid, false otherwise
     */
    public boolean validate(Modifier modifier, PromptType promptType, MathExpression response) {
        return handlers.stream()
                .filter(h -> h.supports(modifier))
                .findFirst()
                .map(h -> applyWithCast(h, modifier, promptType, response))
                .orElse(false);
    }

    @SuppressWarnings("unchecked")
    private <T extends Modifier> boolean applyWithCast(ModifierValidator<?> handler, Modifier modifier, PromptType promptType, MathExpression response) {
        return ((ModifierValidator<T>) handler).validate((T) modifier, promptType, response);
    }
}
