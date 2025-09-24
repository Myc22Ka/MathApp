package pl.myc22ka.mathapp.exercise.exercise.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.ArrayList;
import java.util.List;


/**
 * DTO used for initializing exercises from static data.
 *
 * @param templateExerciseId the associated template exercise ID
 * @param text               the exercise text
 * @param verified           whether the exercise is verified
 * @param context            list of placeholder key-values, {@link PrefixValue}
 * @param rating             the rating exercise
 * @author Myc22Ka
 * @version 1.0.2
 * @since 13.09.2025
 */
public record ExerciseInitializerDTO(
        Long templateExerciseId,
        Long variantExerciseId,
        String text,
        boolean verified,
        Double rating,
        List<PrefixValue> context,
        String answer
) {
}