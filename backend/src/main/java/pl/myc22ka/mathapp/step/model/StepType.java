package pl.myc22ka.mathapp.step.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import static pl.myc22ka.mathapp.model.expression.TemplatePrefix.*;

/**
 * Types of steps used in exercises.
 * Each step type belongs to a category (set, function, or common).
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Getter
@AllArgsConstructor
public enum StepType {
    /**
     * Set union step type.
     */
    SET_UNION(SET),
    /**
     * Set intersection step type.
     */
    SET_INTERSECTION(SET),
    /**
     * Set difference step type.
     */
    SET_DIFFERENCE(SET),
    /**
     * Set find all integers in range step type.
     */
    SET_FIND_ALL_INTEGERS_IN_RANGE(SET),
    /**
     * Set complement step type.
     */
    SET_COMPLEMENT(SET),

    /**
     * Function derivative step type.
     */
    FUNCTION_DERIVATIVE(FUNCTION),

    /**
     * Rest step type.
     */
    REST(COMMON);

    private final TemplatePrefix category;
}