package pl.myc22ka.mathapp.exercise.variant.dto;

import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;

public record TemplateExerciseVariantInitDTO(
        Long templateExerciseId,
        TemplatePrefix category,
        String difficulty,
        String templateText,
        String templateAnswer,
        List<StepWrapper> steps,
        Long exerciseCounter
) {}
