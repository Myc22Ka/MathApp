package pl.myc22ka.mathapp.step.component.steps.sets;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.SetSymbols;
import pl.myc22ka.mathapp.model.set.sets.Interval;
import pl.myc22ka.mathapp.step.component.StepExecutor;
import pl.myc22ka.mathapp.step.component.helper.StepExecutionHelper;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.model.StepWrapper;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;

import java.util.List;

/**
 * Step executor for finding all integers in a given set or interval.
 * <p>
 * If the set is an interval, it returns a set of all integers within that interval.
 * If the set is finite but not an interval, it returns the size of the set.
 * Throws an exception if the set contains infinity.
 * </p>
 *
 * <p>Preconditions:</p>
 * <ul>
 *     <li>The step must have exactly one set in the context.</li>
 *     <li>The set cannot contain infinity.</li>
 * </ul>
 * <p>
 * Result is added to the context with a new generated key.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 17.10.2025
 */
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

        if (SetSymbols.containsInfinity(first.toString())) {
            throw new IllegalArgumentException("Cannot find all integers in a range with infinity: " + first);
        }

        String newKey = helper.nextContextKey(context);

        if (first instanceof Interval interval) {
            var allIntegers = interval.findAllIntegers();

            context.add(new ContextRecord(newKey, allIntegers.getTemplatePrefix(), allIntegers.toString()));
        } else {
            context.add(new ContextRecord(newKey, first.getTemplatePrefix(), first.toString()));
        }
    }
}
