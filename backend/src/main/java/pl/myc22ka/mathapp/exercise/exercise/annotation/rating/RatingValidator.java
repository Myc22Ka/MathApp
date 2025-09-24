package pl.myc22ka.mathapp.exercise.exercise.annotation.rating;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RatingValidator implements ConstraintValidator<Rating, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value < 0.5 || value > 5.0) {
            return false;
        }

        double remainder = value % 0.5;
        return remainder == 0.0;
    }
}
