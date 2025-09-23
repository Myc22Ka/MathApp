package pl.myc22ka.mathapp.step.component;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;

public interface StepExecutor {
    StepType getType();
    void execute(@NotNull StepWrapper step, List<PrefixValue> context);
}
