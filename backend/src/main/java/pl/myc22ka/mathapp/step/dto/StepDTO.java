package pl.myc22ka.mathapp.step.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.model.StepWrapper;

/**
 * Data Transfer Object for Step entity.
 * Represents a single step in a template or variant exercise.
 *
 * @param stepDefinitionId the ID of the step definition
 * @author Myc22Ka
 * @version 1.0.2
 * @since 13.09.2025
 */
@Schema(description = "Represents a step in a template or variant exercise")
public record StepDTO(
        @Schema(description = "Step definition ID", example = "5")
        Long stepDefinitionId,

        @Schema(description = "Step text content", example = "Calculate the union of sets A and B")
        String stepText
) {
    /**
     * Converts a StepWrapper entity to StepDTO.
     *
     * @param step entity to convert
     * @return corresponding StepDTO
     */
    @NotNull
    public static StepDTO fromEntity(@NotNull StepWrapper step) {
        return new StepDTO(
                step.getStepDefinition().getId(),
                step.getStepDefinition().getStepText()
        );
    }

    /**
     * Converts a StepDefinition entity to StepDTO.
     *
     * @param stepDefinition entity to convert
     * @return corresponding StepDTO
     */
    @NotNull
    public static StepDTO fromStepDefinition(@NotNull StepDefinition stepDefinition) {
        return new StepDTO(
                stepDefinition.getId(),
                stepDefinition.getStepText()
        );
    }
}
