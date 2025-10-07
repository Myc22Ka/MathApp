package pl.myc22ka.mathapp.step.component.steps.sets;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.dto.ContextRecord;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.step.component.StepExecutor;
import pl.myc22ka.mathapp.step.component.helper.StepExecutionHelper;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAllIntegers implements StepExecutor {

    private final StepExecutionHelper helper;

    @Override
    public StepType getType() {
        return StepType.SET_FIND_ALL_INTEGERS_IN_RANGE;
    }

    @Override
    public void execute(@NotNull StepWrapper step, List<ContextRecord> context) {
        List<ISet> sets = helper.getSetsFromContext(step, context);

        ISet first = sets.getFirst();

        if (SetSymbols.containsInfinity(first.toString())){
            throw new IllegalArgumentException("Cannot find all integers in a range with infinity: " + first);
        }

        if (first instanceof Interval interval) {
            var allIntegers = interval.findAllIntegers();
            String newKey = helper.nextContextKey(context);

            context.add(new ContextRecord(newKey, allIntegers.getTemplatePrefix(), allIntegers.toString()));
        } else {
            throw new IllegalArgumentException("Expected Interval but got: " + first.getClass().getSimpleName());
        }
    }
}
