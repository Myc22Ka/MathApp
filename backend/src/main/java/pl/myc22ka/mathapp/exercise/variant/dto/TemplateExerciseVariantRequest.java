package pl.myc22ka.mathapp.exercise.variant.dto;

import pl.myc22ka.mathapp.exercise.template.dto.StepDTO;

import java.util.List;

public record TemplateExerciseVariantRequest(
        String templateText,
        String templateAnswer,
        String difficulty,
        List<StepDTO> steps
) {}