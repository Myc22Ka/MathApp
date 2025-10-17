package pl.myc22ka.mathapp.exercise.exercise.dto;

import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;

import java.util.List;


/**
 * DTO used for initializing exercises from static data.
 *
 * @param templateExerciseId the associated template exercise ID
 * @param text               the exercise text
 * @param verified           whether the exercise is verified
 * @param context            list of placeholder key-values, {@link ContextRecord}
 * @param rating             the rating exercise
 * @author Myc22Ka
 * @version 1.0.3
 * @since 13.09.2025
 */
public record ExerciseInitializerDTO(
        Long templateExerciseId,
        Long variantExerciseId,
        String text,
        boolean verified,
        Double rating,
        List<ContextRecord> context,
        String answer
) {
}