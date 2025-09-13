package pl.myc22ka.mathapp.exercise.template.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.template.model.Step;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

public record StepDTO(
        String stepText,
        int orderIndex
) {
    @NotNull
    public static StepDTO fromEntity(@NotNull Step step) {
        return new StepDTO(
                step.getStepText(),
                step.getOrderIndex()
        );
    }

    @NotNull
    public Step toEntityForTemplate(TemplateExercise template) {
        Step step = new Step();
        step.setStepText(this.stepText());
        step.setOrderIndex(this.orderIndex());
        step.setExercise(template);
        return step;
    }

    @NotNull
    public Step toEntityForVariant(TemplateExerciseVariant variant) {
        Step step = new Step();
        step.setStepText(this.stepText());
        step.setOrderIndex(this.orderIndex());
        step.setVariant(variant);
        return step;
    }
}
