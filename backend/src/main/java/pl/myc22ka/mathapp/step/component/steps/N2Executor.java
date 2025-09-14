package pl.myc22ka.mathapp.step.component.steps;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.step.component.StepExecutor;
import pl.myc22ka.mathapp.step.model.Step;
import pl.myc22ka.mathapp.step.model.StepType;

@Component
public class N2Executor implements StepExecutor {
    @Override
    public StepType getType() { return StepType.N2; }

    @Override
    public void execute(@NotNull Step step) {
        System.out.println("Wykonuję logikę N2 dla kroku: " + step.getId());
        // logika N2
    }
}
