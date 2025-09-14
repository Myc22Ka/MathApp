package pl.myc22ka.mathapp.exercise.exercise.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

import java.util.ArrayList;
import java.util.List;


/**
 * DTO used for initializing exercises from static data.
 *
 * @param templateExerciseId the associated template exercise ID
 * @param text               the exercise text
 * @param verified           whether the exercise is verified
 * @param values             list of placeholder values, can be empty
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
public record ExerciseInitializerDTO(
        Long templateExerciseId,
        String text,
        boolean verified,
        List<String> values
) {

    /**
     * Converts this DTO to an Exercise entity.
     *
     * @param dto the ExerciseInitializerDTO
     * @param template the TemplateExercise entity to link
     * @return the built Exercise entity
     */
    public static Exercise fromRecord(@NotNull ExerciseInitializerDTO dto, TemplateExercise template) {
        return Exercise.builder()
                .templateExercise(template)
                .text(dto.text())
                .verified(dto.verified())
                .values(dto.values() != null ? dto.values() : new ArrayList<>())
                .build();
    }
}