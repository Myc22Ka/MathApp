package pl.myc22ka.mathapp.exercise.variant.dto;

import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;

public record TemplateExerciseVariantInitDTO(
        Long templateExerciseId,
        PromptType category,
        String difficulty,
        String templateText,
        String templateAnswer,
        List<StepWrapper> steps
) {}
