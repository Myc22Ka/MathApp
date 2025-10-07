package pl.myc22ka.mathapp.ai.prompt.validator;

import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Interface for validating specific types of Modifiers.
 *
 * @param <T> the type of Modifier this validator supports
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
public interface ModifierValidator<T extends Modifier> {

    /**
     * Checks if this validator supports the given modifier instance.
     *
     * @param modifier the modifier to check
     * @return true if supported, false otherwise
     */
    boolean supports(Modifier modifier);

    /**
     * Validates the given modifier against the prompt type and user response.
     *
     * @param modifier   the modifier to validate
     * @param type       the prompt type context
     * @param response   the user's response expression
     * @return true if validation passes, false otherwise
     */
    boolean validate(T modifier, TemplatePrefix type, MathExpression response);
}