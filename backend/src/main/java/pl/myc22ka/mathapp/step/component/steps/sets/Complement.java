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
 * Step executor for performing the complement operation on sets.
 * <p>
 * This step expects exactly two sets in the context:
 * <ol>
 *     <li>The first set to calculate the complement of.</li>
 *     <li>The universal set in which to calculate the complement.</li>
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
public class Complement implements StepExecutor {

    private final StepExecutionHelper helper;

    @Override
    public StepType getType() {
        return StepType.SET_COMPLEMENT;
    }

    @Override
    public void execute(@NotNull StepWrapper step, List<ContextRecord> context) {
        List<ISet> sets = helper.getSetsFromContext(step, context);
        helper.ensureTwoSets(sets);

        ISet first = sets.get(0);
        ISet uniwersum = sets.get(1);

        ISet complement = first.complement(uniwersum);

        String newKey = helper.nextContextKey(context);
        context.add(new ContextRecord(newKey, complement.getTemplatePrefix(), complement.toString()));
    }
}
