package pl.myc22ka.mathapp.exercise.variant.dto;

import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;

/**
 * Data Transfer Object (DTO) for initializing TemplateExerciseVariant
 * entities from JSON files during application startup.
 *
 * @param templateExerciseId the ID of the parent {@link pl.myc22ka.mathapp.exercise.template.model.TemplateExercise}
 * @param category           the mathematical category or prefix of the exercise (e.g. ALGEBRA, TRIGONOMETRY)
 * @param difficulty         the difficulty level of the variant (e.g. EASY, MEDIUM, HARD)
 * @param templateText       the template text of the exercise variant containing placeholders
 * @param templateAnswer     the answer pattern or expression for the variant
 * @param steps              the list of {@link pl.myc22ka.mathapp.step.model.StepWrapper} objects defining how the variant is built or solved
 * @param exerciseCounter    the counter tracking how many exercises were generated from this variant
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
public record TemplateExerciseVariantInitDTO(
        Long templateExerciseId,
        TemplatePrefix category,
        String difficulty,
        String templateText,
        String templateAnswer,
        List<StepWrapper> steps,
        Long exerciseCounter
) {
}
