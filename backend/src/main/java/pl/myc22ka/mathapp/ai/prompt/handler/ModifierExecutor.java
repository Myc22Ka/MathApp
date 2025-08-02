package pl.myc22ka.mathapp.ai.prompt.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.model.expression.MathExpression;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ModifierExecutor {

    private final List<ModifierHandler<? extends Modifier>> handlers;

    public boolean applyModifier(Modifier modifier, PromptType promptType, MathExpression response) {
        return handlers.stream()
                .filter(h -> h.supports(modifier))
                .findFirst()
                .map(h -> applyWithCast(h, modifier, promptType, response))
                .orElse(false);
    }

    @SuppressWarnings("unchecked")
    private <T extends Modifier> boolean applyWithCast(ModifierHandler<?> handler, Modifier modifier, PromptType promptType, MathExpression response) {
        return ((ModifierHandler<T>) handler).apply((T) modifier, promptType, response);
    }
}
