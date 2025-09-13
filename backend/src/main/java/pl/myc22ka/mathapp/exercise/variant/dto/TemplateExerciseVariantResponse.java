package pl.myc22ka.mathapp.exercise.variant.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.template.dto.StepDTO;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.List;

public record TemplateExerciseVariantResponse(
        Long id,
        Long templateExerciseId,
        String category,
        String difficulty,
        String templateText,
        String templateAnswer,
        String clearText,
        List<StepDTO> steps,
        Long exerciseCounter
) {
    @NotNull
    public static TemplateExerciseVariantResponse fromEntity(@NotNull TemplateExerciseVariant variant) {
        return new TemplateExerciseVariantResponse(
                variant.getId(),
                variant.getTemplateExercise() != null ? variant.getTemplateExercise().getId() : null,
                variant.getCategory() != null ? variant.getCategory().name() : null,
                variant.getDifficulty(),
                variant.getTemplateText(),
                variant.getTemplateAnswer(),
                variant.getClearText(),
                variant.getSteps() != null
                        ? variant.getSteps().stream().map(StepDTO::fromEntity).toList()
                        : List.of(),
                variant.getExerciseCounter()
        );
    }
}
