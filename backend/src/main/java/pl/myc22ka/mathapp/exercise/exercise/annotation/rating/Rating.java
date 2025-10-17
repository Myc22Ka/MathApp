package pl.myc22ka.mathapp.exercise.exercise.annotation.rating;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation for ratings.
 * <p>
 * Ensures that a numeric rating is between 1 and 5 (inclusive)
 * in increments of 0.5.
 * <p>
 * Can be applied to fields or method parameters.
 * Uses {@link RatingValidator} to enforce the constraint.
 * <p>
 * Example usage:
 * <pre>
 *     {@code
 *     @Rating
 *     private Double userRating;
 *     }
 * </pre>
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RatingValidator.class)
@Documented
public @interface Rating {
    String message() default "Rating must be between 1 and 5 in 0.5 increments";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
