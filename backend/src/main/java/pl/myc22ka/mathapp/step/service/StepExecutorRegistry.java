package pl.myc22ka.mathapp.step.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.step.component.StepExecutor;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class StepExecutorRegistry {

    private final Map<StepType, StepExecutor> executors = new EnumMap<>(StepType.class);

    public StepExecutorRegistry(@NotNull List<StepExecutor> beans) {
        for (StepExecutor executor : beans) {
            executors.put(executor.getType(), executor);
        }
    }

    public void executeStep(@NotNull StepWrapper step, List<PrefixValue> context) {
        StepExecutor executor = executors.get(step.getStepDefinition().getStepType());
        if (executor == null) {
            throw new IllegalArgumentException("Brak implementacji dla kroku: " + step.getStepDefinition().getStepType());
        }
        executor.execute(step, context);
    }
}
