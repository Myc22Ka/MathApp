package pl.myc22ka.mathapp.exercise.template.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.template.model.Step;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

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
    public Step toEntity(TemplateExercise exercise) {
        Step step = new Step();
        step.setStepText(this.stepText());
        step.setOrderIndex(this.orderIndex());
        step.setExercise(exercise); // ustawienie relacji
        return step;
    }
}
