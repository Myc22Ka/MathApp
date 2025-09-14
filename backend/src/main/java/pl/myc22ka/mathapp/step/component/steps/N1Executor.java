package pl.myc22ka.mathapp.step.component.steps;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.step.model.Step;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.component.StepExecutor;

@Component
public class N1Executor implements StepExecutor {
    @Override
    public StepType getType() { return StepType.N1; }

    @Override
    public void execute(@NotNull Step step) {
        System.out.println("Wykonuję logikę N1 dla kroku: " + step.getStepText());
        // logika N1
    }
}