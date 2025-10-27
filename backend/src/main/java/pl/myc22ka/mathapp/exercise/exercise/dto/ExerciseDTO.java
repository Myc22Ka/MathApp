package pl.myc22ka.mathapp.exercise.exercise.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.topic.model.Topic;

/**
 * Data Transfer Object for Exercise.
 * Contains only essential fields for API or service responses.
 *
 * @param id                 the exercise ID
 * @param text               the exercise text
 * @author Myc22Ka
 * @version 1.0.4
 * @since 13.09.2025
 */
@Schema(description = "DTO representing an exercise with its essential fields.")
public record ExerciseDTO(

        @Schema(description = "The unique ID of the exercise", example = "123")
        Long id,

        @Schema(description = "The text of the exercise", example = "Solve the equation: 2x + 3 = 7")
        String text,

        @Schema(description = "The flag that shows if exercise passed through every given modifier", example = "false")
        boolean verified,

        @Schema(description = "The rating of the exercise", example = "4.5")
        Double rating,

        String topic,

        Boolean isSolved

) {

    /**
     * Converts an Exercise entity to an ExerciseDTO.
     *
     * @param exercise the Exercise entity
     * @return the ExerciseDTO with relevant fields
     */
    @NotNull
    public static ExerciseDTO fromEntity(@NotNull Exercise exercise, Boolean isSolved) {
        return new ExerciseDTO(
                exercise.getId(),
                exercise.getText(),
                exercise.isVerified(),
                exercise.getRating(),
                exercise.getTemplateOrVariant().getCategory().name(),
                isSolved
        );
    }
}