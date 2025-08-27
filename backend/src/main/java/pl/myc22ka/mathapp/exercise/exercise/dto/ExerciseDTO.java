package pl.myc22ka.mathapp.exercise.exercise.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;

public record ExerciseDTO(
        Long id,
        Long templateExerciseId,
        String text
) {
    @NotNull
    public static ExerciseDTO fromEntity(@NotNull Exercise exercise) {
        return new ExerciseDTO(
                exercise.getId(),
                exercise.getTemplateExercise() != null ? exercise.getTemplateExercise().getId() : null,
                exercise.getText()
        );
    }
}
