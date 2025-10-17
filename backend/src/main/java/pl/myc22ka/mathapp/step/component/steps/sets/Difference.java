package pl.myc22ka.mathapp.step.component.steps.sets;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.step.component.StepExecutor;
import pl.myc22ka.mathapp.step.component.helper.StepExecutionHelper;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.model.StepWrapper;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;

import java.util.List;


/**
 * Step executor for performing the set difference operation.
 * <p>
 * This step expects exactly two sets in the context:
 * <ol>
 *     <li>The first set from which elements will be removed.</li>
 *     <li>The second set containing elements to remove from the first set.</li>
 * </ol>
 * The result is added to the context with a new context key.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 17.10.2025
 */
@Component
@RequiredArgsConstructor
public class Difference implements StepExecutor {

    private final StepExecutionHelper helper;

    @Override
    public StepType getType() {
        return StepType.SET_DIFFERENCE;
    }

    @Override
    public void execute(@NotNull StepWrapper step, List<ContextRecord> context) {
        List<ISet> sets = helper.getSetsFromContext(step, context);
        helper.ensureTwoSets(sets);

        ISet first = sets.get(0);
        ISet second = sets.get(1);

        ISet difference = first.difference(second);

        String newKey = helper.nextContextKey(context);
        context.add(new ContextRecord(newKey, difference.getTemplatePrefix(), difference.toString()));
    }
}
