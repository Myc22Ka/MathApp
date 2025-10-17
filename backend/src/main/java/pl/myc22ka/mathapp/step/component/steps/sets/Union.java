package pl.myc22ka.mathapp.step.component.steps.sets;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.step.component.helper.StepExecutionHelper;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.component.StepExecutor;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;


/**
 * Step executor for computing the union of two sets.
 * <p>
 * Preconditions:
 * <ul>
 *     <li>The step must have exactly two sets in the context.</li>
 * </ul>
 * The result of the union is added to the context with a newly generated key.
 * </p>
 *
 * <p>This step corresponds to the {@link StepType#SET_UNION} type.</p>
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 14.09.2025
 */
@Component
@RequiredArgsConstructor
public class Union implements StepExecutor {

    private final StepExecutionHelper helper;

    @Override
    public StepType getType() {
        return StepType.SET_UNION;
    }

    @Override
    public void execute(@NotNull StepWrapper step, List<ContextRecord> context) {
        List<ISet> sets = helper.getSetsFromContext(step, context);
        helper.ensureTwoSets(sets);

        ISet first = sets.get(0);
        ISet second = sets.get(1);

        ISet union = first.union(second);

        String newKey = helper.nextContextKey(context);
        context.add(new ContextRecord(newKey, union.getTemplatePrefix(), union.toString()));
    }
}