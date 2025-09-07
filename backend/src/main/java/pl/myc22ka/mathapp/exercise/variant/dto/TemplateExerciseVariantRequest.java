package pl.myc22ka.mathapp.exercise.variant.dto;

import pl.myc22ka.mathapp.exercise.template.model.Step;

import java.util.List;

public record TemplateExerciseVariantRequest(
        String difficulty,
        String text,
        String answer,
        List<Step> steps
) {}
