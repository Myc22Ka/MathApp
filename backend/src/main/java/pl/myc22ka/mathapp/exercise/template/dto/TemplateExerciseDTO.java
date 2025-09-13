package pl.myc22ka.mathapp.exercise.template.dto;


import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

import java.util.List;

public record TemplateExerciseDTO(
        Long id,
        String category,
        String difficulty,
        String templateText,
        String templateAnswer,
        List<StepDTO> steps
) {
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
