package pl.myc22ka.mathapp.step.component;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;

/**
 * Interface defining a contract for executing a step in an exercise workflow.
 * <p>
 * Implementations correspond to specific {@link StepType} operations
 * (e.g., union, intersection, complement) and modify the provided context.
 * </p>
 * <p>
 * Each executor is responsible for:
 * <ul>
 *     <li>Specifying its {@link StepType} via {@link #getType()}.</li>
 *     <li>Performing the operation defined by the step and updating the context.</li>
 * </ul>
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
public interface StepExecutor {

    /**
     * Returns the {@link StepType} this executor handles.
     *
     * @return the step type
     */
    StepType getType();

    /**
     * Executes the step, applying its logic to the provided context.
     *
     * @param step    the step wrapper containing configuration and prefixes
     * @param context the list of context records containing prior results; may be modified
     */
    void execute(@NotNull StepWrapper step, List<ContextRecord> context);
}
