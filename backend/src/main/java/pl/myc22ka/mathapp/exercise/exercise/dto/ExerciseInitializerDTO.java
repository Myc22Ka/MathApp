package pl.myc22ka.mathapp.exercise.exercise.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

import java.util.ArrayList;
import java.util.List;

public record ExerciseInitializerDTO(
        Long templateExerciseId,
        String text,
        boolean verified,
        List<String> values
) {
    public static Exercise fromRecord(@NotNull ExerciseInitializerDTO dto, TemplateExercise template) {
        return Exercise.builder()
                .templateExercise(template)
                .text(dto.text())
                .verified(dto.verified())
                .values(dto.values() != null ? dto.values() : new ArrayList<>())
                .build();
    }
}