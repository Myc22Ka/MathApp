package pl.myc22ka.mathapp.exercise.exercise.annotation.rating;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RatingValidator.class)
@Documented
public @interface Rating {
    String message() default "Rating must be between 1 and 5 in 0.5 increments";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
