package pl.myc22ka.mathapp.step.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.step.component.StepExecutor;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Service that keeps track of all available {@link StepExecutor} implementations.
 * Maps each {@link StepType} to its corresponding executor for easy lookup and execution.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Service
public class StepExecutorRegistry {

    private final Map<StepType, StepExecutor> executors = new EnumMap<>(StepType.class);

    /**
     * Registers all provided step executors in a map by their {@link StepType}.
     *
     * @param beans the list of all available {@link StepExecutor} beans
     */
    public StepExecutorRegistry(@NotNull List<StepExecutor> beans) {
        for (StepExecutor executor : beans) {
            executors.put(executor.getType(), executor);
        }
    }

    /**
     * Executes the step using the appropriate executor based on its type.
     *
     * @param step    the step to execute
     * @param context the current execution context
     * @throws IllegalArgumentException if no executor exists for the step's type
     */
    public void executeStep(@NotNull StepWrapper step, List<ContextRecord> context) {
        StepExecutor executor = executors.get(step.getStepDefinition().getStepType());
        if (executor == null) {
            throw new IllegalArgumentException("Brak implementacji dla kroku: " + step.getStepDefinition().getStepType());
        }
        executor.execute(step, context);
    }
}
