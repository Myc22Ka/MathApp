package pl.myc22ka.mathapp.step.component;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.step.model.Step;
import pl.myc22ka.mathapp.step.model.StepType;

import java.util.List;

public interface StepExecutor {
    StepType getType();
    void execute(@NotNull Step step, List<PrefixValue> context);
}
