package pl.myc22ka.mathapp.modifier.validator;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.model.set.ISet;

/**
 * Interface for validating specific types of Modifiers.
 *
 * @param <T> the type of Modifier this validator supports
 * @author Myc22Ka
 * @version 1.0.2
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
     * @param modifier the modifier to validate
     * @param type     the prompt type context
     * @param response the user's response expression
     * @return true if validation passes, false otherwise
     */
    boolean validate(T modifier, TemplatePrefix type, MathExpression response);

    /**
     * Validates a set response against the difficulty level of the modifier.
     *
     * @param modifier the difficulty modifier
     * @param set      the AI-generated set
     * @return true if the set meets the required difficulty level, false otherwise
     */
    boolean validateSets(@NotNull T modifier, ISet set);
}