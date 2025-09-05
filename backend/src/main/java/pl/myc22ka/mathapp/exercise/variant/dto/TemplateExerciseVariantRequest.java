package pl.myc22ka.mathapp.exercise.variant.dto;

public record TemplateExerciseVariantRequest(
        String difficulty,
        String text,
        String answer
) {}
