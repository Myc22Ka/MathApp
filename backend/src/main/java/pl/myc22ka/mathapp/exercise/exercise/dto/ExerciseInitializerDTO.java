package pl.myc22ka.mathapp.exercise.exercise.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
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
 * @param context             list of placeholder key-values, {@link PrefixValue}
 * @author Myc22Ka
 * @version 1.0.1
 * @since 13.09.2025
 */
public record ExerciseInitializerDTO(
        Long templateExerciseId,
        String text,
        boolean verified,
        List<PrefixValue> context,
        String answer
) {

    /**
     * Converts this DTO to an Exercise entity.
     *
     * @param dto the ExerciseInitializerDTO
     * @param template the TemplateExercise entity to link
     * @return the built Exercise entity
     */
    public static Exercise fromRecord(@NotNull ExerciseInitializerDTO dto,
                                      TemplateExercise template,
                                      @NotNull ObjectMapper objectMapper) {
        String contextJson;
        try {
            contextJson = objectMapper.writeValueAsString(dto.context() != null ? dto.context() : List.of());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize context for exercise initializer", e);
        }

        return Exercise.builder()
                .templateExercise(template)
                .text(dto.text())
                .verified(dto.verified())
                .contextJson(contextJson)
                .answer(dto.answer())
                .build();
    }
}