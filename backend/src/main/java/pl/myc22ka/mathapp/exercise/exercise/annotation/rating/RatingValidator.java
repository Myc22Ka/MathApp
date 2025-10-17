package pl.myc22ka.mathapp.exercise.exercise.annotation.rating;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link Rating} annotation.
 * <p>
 * Checks that a Double value is:
 * <ul>
 *     <li>between 1.0 and 5.0 (inclusive)</li>
 *     <li>a multiple of 0.5</li>
 * </ul>
 * <p>
 * Null values are considered valid.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
public class RatingValidator implements ConstraintValidator<Rating, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value < 1.0 || value > 5.0) {
            return false;
        }

        double scaled = value * 2;
        return Math.abs(scaled - Math.round(scaled)) < 1e-9;
    }
}
