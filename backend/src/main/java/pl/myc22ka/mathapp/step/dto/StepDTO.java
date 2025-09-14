package pl.myc22ka.mathapp.step.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.step.model.Step;
import pl.myc22ka.mathapp.step.model.StepType;

/**
 * Data Transfer Object for Step entity.
 * Represents a single step in a template or variant exercise.
 *
 * @param stepType   the type of the step (enum that defines logic + description)
 * @param orderIndex the order of the step within the exercise
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Schema(description = "Represents a step in a template or variant exercise")
public record StepDTO(

        @Schema(description = "Step type identifier", example = "N1")
        StepType stepType,

        @Schema(description = "Order of the step in the sequence", example = "1")
        int orderIndex
) {
    /**
     * Converts a Step entity to StepDTO.
     *
     * @param step entity to convert
     * @return corresponding StepDTO
     */
    @NotNull
    public static StepDTO fromEntity(@NotNull Step step) {
        return new StepDTO(
                step.getStepType(),
                step.getOrderIndex()
        );
    }

    /**
     * Converts this DTO to a Step entity linked to a TemplateExercise.
     *
     * @param template the parent TemplateExercise
     * @return a new Step entity
     */
    @NotNull
    public Step toEntityForTemplate(TemplateExercise template) {
        return Step.builder()
                .stepType(this.stepType())
                .stepText(this.stepType().getDescription())
                .orderIndex(this.orderIndex())
                .exercise(template)
                .build();
    }

    /**
     * Converts this DTO to a Step entity linked to a TemplateExerciseVariant.
     *
     * @param variant the parent TemplateExerciseVariant
     * @return a new Step entity
     */
    @NotNull
    public Step toEntityForVariant(TemplateExerciseVariant variant) {
        return Step.builder()
                .stepType(this.stepType())
                .stepText(this.stepType().getDescription())
                .orderIndex(this.orderIndex())
                .variant(variant)
                .build();
    }
}
