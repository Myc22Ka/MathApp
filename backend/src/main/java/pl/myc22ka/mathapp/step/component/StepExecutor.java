package pl.myc22ka.mathapp.step.component;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.step.model.Step;
import pl.myc22ka.mathapp.step.model.StepType;

public interface StepExecutor {
    StepType getType();
    void execute(@NotNull Step step);
}
