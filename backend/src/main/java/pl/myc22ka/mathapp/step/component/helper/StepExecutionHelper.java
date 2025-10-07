package pl.myc22ka.mathapp.step.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.dto.ContextRecord;
import pl.myc22ka.mathapp.ai.prompt.dto.TemplateString;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.model.StepWrapper;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StepExecutionHelper {

    private final ExpressionFactory expressionFactory;
    private final StepDefinitionRepository stepDefinitionRepository;

    public StepDefinition getStepDefinition(Long id) {
        return stepDefinitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "StepDefinition not found with id: " + id));
    }

    public List<ISet> getSetsFromContext(@NotNull StepWrapper step, @NotNull List<ContextRecord> context) {
        Map<String, String> contextMap = context.stream()
                .collect(Collectors.toMap(c -> c.key().templateString(), ContextRecord::value));

        List<ISet> sets = new ArrayList<>();

        for (String prefix : step.getPrefixes()) {
            if (contextMap.containsKey(prefix)) {
                String value = contextMap.get(prefix);
                MathExpression expr = expressionFactory.parse(value);
                if (expr instanceof ISet setExpr) {
                    sets.add(setExpr);
                }
            }
        }

        return sets;
    }

    /**
     * Generates the next available context key like context1, context2, etc.
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

    public void ensureTwoSets(@NotNull List<ISet> sets) {
        if (sets.size() != 2) {
            throw new IllegalArgumentException(
                    "Step requires exactly two sets. Found: " + sets.size()
            );
        }
    }
}
