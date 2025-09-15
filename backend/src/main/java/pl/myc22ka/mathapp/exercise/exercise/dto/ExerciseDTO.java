package pl.myc22ka.mathapp.exercise.exercise.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;

/**
 * Data Transfer Object for Exercise.
 * Contains only essential fields for API or service responses.
 *
 * @param id                 the exercise ID
 * @param templateExerciseId the associated template exercise ID, can be null
 * @param text               the exercise text
 * @author Myc22Ka
 * @version 1.0.1
 * @since 13.09.2025
 */
@Schema(description = "DTO representing an exercise with its essential fields.")
public record ExerciseDTO(

        @Schema(description = "The unique ID of the exercise", example = "123")
        Long id,

        @Schema(description = "The ID of the associated template exercise (nullable)", example = "45")
        Long templateExerciseId,

        @Schema(description = "The text of the exercise", example = "Solve the equation: 2x + 3 = 7")
        String text,

        @Schema(description = "The answer of the exercise", example = "x = 2")
        String answer

) {

    /**
     * Converts an Exercise entity to an ExerciseDTO.
     *
     * @param exercise the Exercise entity
     * @return the ExerciseDTO with relevant fields
     */
    @NotNull
    public static ExerciseDTO fromEntity(@NotNull Exercise exercise) {
        return new ExerciseDTO(
                exercise.getId(),
                exercise.getTemplateExercise() != null ? exercise.getTemplateExercise().getId() : null,
                exercise.getText(),
                exercise.getAnswer()
        );
    }
}