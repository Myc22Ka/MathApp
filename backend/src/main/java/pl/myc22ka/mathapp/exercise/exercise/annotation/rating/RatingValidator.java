package pl.myc22ka.mathapp.exercise.exercise.annotation.rating;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RatingValidator implements ConstraintValidator<Rating, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // jeśli null jest OK
        }

        // Zakres 1.0–5.0
        if (value < 1.0 || value > 5.0) {
            return false;
        }

        // Czy to wielokrotność 0.5
        double scaled = value * 2;
        return Math.abs(scaled - Math.round(scaled)) < 1e-9;
    }
}
