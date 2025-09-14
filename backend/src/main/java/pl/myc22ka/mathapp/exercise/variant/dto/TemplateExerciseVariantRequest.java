package pl.myc22ka.mathapp.exercise.variant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import pl.myc22ka.mathapp.exercise.template.dto.StepDTO;

import java.util.List;

/**
 * Request DTO for creating or updating a template exercise variant.
 *
 * @param templateText   the text of the variant template (with template strings)
 * @param templateAnswer the expected answer for the variant
 * @param difficulty     difficulty level (e.g., 1, 2, 3)
 * @param steps          ordered list of steps
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Schema(description = "Request object for creating or updating a template exercise variant")
public record TemplateExerciseVariantRequest(
        @Schema(description = "Template text with template strings", example = "Solve: ${s1} + ${s2}")
        String templateText,

        @Schema(description = "Expected answer as template string", example = "${s1}")
        String templateAnswer,

        @Schema(description = "Difficulty level of the variant", example = "1")
        String difficulty,

        @Schema(description = "List of steps describing how to solve the variant")
        List<StepDTO> steps
) {
}