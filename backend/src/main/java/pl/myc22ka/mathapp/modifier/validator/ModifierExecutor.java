package pl.myc22ka.mathapp.modifier.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.List;

/**
 * Executes validation for given Modifier using matching Validator.
 * <p>
 * Delegates to first supporting ModifierValidator from injected handlers.
 *
 * @author Myc22Ka
 * @version 1.0.2
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
     * @param type the prompt type context
     * @param response   the user's response expression
     * @return true if valid, false otherwise
     */
    public boolean validate(Modifier modifier, TemplatePrefix type, MathExpression response) {
        return handlers.stream()
                .filter(h -> h.supports(modifier))
                .findFirst()
                .map(h -> applyWithCast(h, modifier, type, response))
                .orElse(false);
    }


    /**
     * Applies a generic {@link ModifierValidator}.
     *
     * @param handler  the validator
     * @param modifier the modifier to validate
     * @param type     the prompt type
     * @param response the AI-generated response
     * @param <T>      the type of modifier
     * @return true if validation passes, false otherwise
     */
    @SuppressWarnings("unchecked")
    private <T extends Modifier> boolean applyWithCast(ModifierValidator<?> handler, Modifier modifier, TemplatePrefix type, MathExpression response) {
        return ((ModifierValidator<T>) handler).validate((T) modifier, type, response);
    }
}
