package pl.myc22ka.mathapp.step.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.utils.resolver.dto.TemplateString;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.model.StepWrapper;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility component responsible for assisting in the execution of template exercise steps.
 * <p>
 * Handles parsing of context records into {@link MathExpression} instances,
 * fetching of {@link StepDefinition}s, and validation of required sets within a step.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Component
@RequiredArgsConstructor
public class StepExecutionHelper {

    private final ExpressionFactory expressionFactory;
    private final StepDefinitionRepository stepDefinitionRepository;

    /**
     * Retrieves a {@link StepDefinition} by its ID, throwing an exception if not found.
     *
     * @param id the step definition ID
     * @return the corresponding {@link StepDefinition}
     * @throws IllegalArgumentException if no definition is found for the given ID
     */
    public StepDefinition getStepDefinition(Long id) {
        return stepDefinitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "StepDefinition not found with id: " + id));
    }

    /**
     * Extracts and parses all sets from the given step’s prefixes within the provided context.
     * <p>
     * The method maps context keys to their corresponding records,
     * filters only those prefixes that exist in the context,
     * and uses the {@link ExpressionFactory} to parse them into {@link MathExpression} objects.
     * Only expressions that implement {@link ISet} are included in the result.
     * </p>
     *
     * @param step    the step containing the prefixes to look up
     * @param context the full list of context records available to the step
     * @return list of {@link ISet} instances found in the context
     */
    public List<ISet> getSetsFromContext(@NotNull StepWrapper step, @NotNull List<ContextRecord> context) {
        Map<String, ContextRecord> contextMap = context.stream()
                .collect(Collectors.toMap(c -> c.key().templateString(), Function.identity()));

        List<ISet> sets = new ArrayList<>();

        for (String prefix : step.getPrefixes()) {
            if (contextMap.containsKey(prefix)) {
                ContextRecord record = contextMap.get(prefix);

                MathExpression expr = expressionFactory.parse(record);

                if (expr instanceof ISet setExpr) {
                    sets.add(setExpr);
                }
            }
        }

        return sets;
    }

    /**
     * Generates the next available context key following the pattern: {@code context1}, {@code context2}, etc.
     *
     * @param context the list of context records
     * @return the next unique context key (e.g., {@code "context3"})
     */
    public String nextContextKey(@NotNull List<ContextRecord> context) {
        int max = context.stream()
                .map(ContextRecord::key)
                .map(TemplateString::templateString)
                .filter(k -> k.startsWith("context"))
                .map(k -> k.substring(7))
                .filter(s -> s.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return "context" + (max + 1);
    }

    /**
     * Ensures that exactly two sets are available for a step operation.
     *
     * @param sets the list of sets to validate
     * @throws IllegalArgumentException if the number of sets is not equal to two
     */
    public void ensureTwoSets(@NotNull List<ISet> sets) {
        if (sets.size() != 2) {
            throw new IllegalArgumentException(
                    "Step requires exactly two sets. Found: " + sets.size()
            );
        }
    }
}
