package pl.myc22ka.mathapp.exercise.variant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.step.dto.StepDTO;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.List;

/**
 * Response DTO for returning template exercise variant data.
 *
 * @param id                 unique identifier of the variant
 * @param templateExerciseId id of the parent template exercise
 * @param category           category of the variant (from PromptType)
 * @param difficulty         difficulty level of the variant
 * @param templateText       text of the variant template with placeholders
 * @param templateAnswer     expected answer for the variant
 * @param steps              ordered list of steps associated with the variant
 * @param exerciseCounter    number of exercises generated from this variant
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Schema(description = "Response object representing a template exercise variant")
public record TemplateExerciseVariantResponse(
        @Schema(description = "Unique identifier of the variant", example = "1")
        Long id,

        @Schema(description = "Parent template exercise ID", example = "10")
        Long templateExerciseId,

        @Schema(description = "Main Topic", example = "SET")
        String category,

        @Schema(description = "Difficulty level of the variant", example = "1")
        String difficulty,

        @Schema(description = "Template text with template strings", example = "Solve: ${s1} u ${s2}")
        String templateText,

        @Schema(description = "Expected answer as a template string", example = "${s1}")
        String templateAnswer,

        @Schema(description = "Steps associated with the variant")
        List<StepDTO> steps,

        @Schema(description = "Number of exercises generated from this variant", example = "3")
        Long exerciseCounter
) {
    /**
     * Maps a {@link TemplateExerciseVariant} entity to this response DTO.
     *
     * @param variant entity to convert
     * @return corresponding response DTO
     */
    @NotNull
    public static TemplateExerciseVariantResponse fromEntity(@NotNull TemplateExerciseVariant variant) {
        return new TemplateExerciseVariantResponse(
                variant.getId(),
                variant.getTemplateExercise() != null ? variant.getTemplateExercise().getId() : null,
                variant.getCategory() != null ? variant.getCategory().name() : null,
                variant.getDifficulty(),
                variant.getTemplateText(),
                variant.getTemplateAnswer(),
                variant.getSteps() != null
                        ? variant.getSteps().stream().map(StepDTO::fromEntity).toList()
                        : List.of(),
                variant.getExerciseCounter()
        );
    }
}
