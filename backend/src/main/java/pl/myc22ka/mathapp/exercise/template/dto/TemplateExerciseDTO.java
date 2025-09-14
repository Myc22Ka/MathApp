package pl.myc22ka.mathapp.exercise.template.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.step.dto.StepDTO;

import java.util.List;

/**
 * Data Transfer Object (DTO) for {@link TemplateExercise}.
 * Encapsulates the data of a template exercise including metadata, text,
 * answer, and associated steps.
 *
 * @param id             unique identifier of the template exercise
 * @param category       category of the exercise (mapped from {@link PromptType})
 * @param difficulty     difficulty level (custom string)
 * @param templateText   raw text of the template containing placeholders
 * @param templateAnswer expected answer pattern for the template
 * @param steps          ordered list of steps for solving the exercise
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Schema(description = "Represents a template exercise with metadata, text, answer, and steps")
public record TemplateExerciseDTO(

        @Schema(description = "Unique identifier of the template exercise", example = "1")
        Long id,

        @Schema(description = "Category of the template exercise", example = "SET")
        String category,

        @Schema(description = "Difficulty level of the exercise", example = "1")
        String difficulty,

        @Schema(description = "Template text with placeholders", example = "Solve: ${s1} + ${s2}")
        String templateText,

        @Schema(description = "Expected answer format", example = "${s1}")
        String templateAnswer,

        @Schema(description = "Steps describing how to solve the exercise in order")
        List<StepDTO> steps
) {
    /**
     * Maps a {@link TemplateExercise} entity to a {@link TemplateExerciseDTO}.
     *
     * @param exercise entity to convert
     * @return corresponding DTO
     */
    @NotNull
    public static TemplateExerciseDTO fromEntity(@NotNull TemplateExercise exercise) {
        return new TemplateExerciseDTO(
                exercise.getId(),
                exercise.getCategory().name(),
                exercise.getDifficulty(),
                exercise.getTemplateText(),
                exercise.getTemplateAnswer(),
                exercise.getSteps() != null
                        ? exercise.getSteps().stream()
                        .map(StepDTO::fromEntity)
                        .toList()
                        : List.of()
        );
    }

    /**
     * Converts this DTO into a {@link TemplateExercise} entity.
     * Steps are mapped back and linked to the template exercise.
     *
     * @return new {@link TemplateExercise} entity
     */
    @NotNull
    public TemplateExercise toEntity() {
        TemplateExercise exercise = new TemplateExercise();
        exercise.setId(this.id());
        exercise.setCategory(PromptType.valueOf(this.category()));
        exercise.setDifficulty(this.difficulty());
        exercise.setTemplateText(this.templateText());
        exercise.setTemplateAnswer(this.templateAnswer());

        if (this.steps() != null) {
            exercise.getSteps().addAll(
                    this.steps().stream()
                            .map(stepDto -> stepDto.toEntityForTemplate(exercise))
                            .toList()
            );
        }

        return exercise;
    }
}