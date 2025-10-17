package pl.myc22ka.mathapp.exercise.template.component;

import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;

/**
 * Represents a template or variant for generating exercises.
 * <p>
 * Provides access to basic template properties, steps, and exercise counter.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
public interface TemplateLike {

    /**
     * Returns the category (topic type) of the template.
     *
     * @return the {@link TemplatePrefix} representing the template's category
     */
    TemplatePrefix getCategory();

    /**
     * Returns the difficulty level of the template.
     *
     * @return difficulty as a string
     */
    String getDifficulty();

    /**
     * Returns the template text used for exercise generation.
     *
     * @return template text
     */
    String getTemplateText();

    /**
     * Returns the template answer associated with this template.
     *
     * @return answer as a string
     */
    String getTemplateAnswer();

    /**
     * Returns the "clear" version of the template text without placeholders.
     *
     * @return clear text
     */
    String getClearText();

    /**
     * Returns the list of steps for this template.
     *
     * @return list of {@link StepWrapper} objects
     */
    List<StepWrapper> getSteps();

    /**
     * Returns the number of exercises generated from this template.
     *
     * @return exercise counter
     */
    Long getExerciseCounter();

    /**
     * Sets the number of exercises generated from this template.
     *
     * @param counter the new exercise counter
     */
    void setExerciseCounter(Long counter);
}
