package pl.myc22ka.mathapp.ai.prompt.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.validator.ModifierExecutor;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromptHelper {

    private final ModifierExecutor modifierExecutor;

    public boolean verify(MathExpression expression, TemplatePrefix type, @NotNull List<Modifier> modifiers) {
        for (Modifier modifier : modifiers) {
            if (!modifierExecutor.validate(modifier, type, expression)) {
                return false;
            }
        }
        return true;
    }
}
