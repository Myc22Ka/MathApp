package pl.myc22ka.mathapp.ai.prompt.handler.handlers;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.handler.ModifierHandler;
import pl.myc22ka.mathapp.ai.prompt.handler.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.ai.prompt.service.ModifierService;
import pl.myc22ka.mathapp.exceptions.custom.PromptValidatorException;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.utils.checker.SetChecker;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TemplateModifierHandler implements ModifierHandler<TemplateModifier> {

    private final ModifierService modifierService;
    private final SetChecker setChecker;
    private final TemplateResolver templateResolver;

    @Override
    public boolean supports(Modifier modifier) {
        return modifier instanceof TemplateModifier;
    }

    @Override
    public boolean apply(TemplateModifier modifier, PromptType promptType, MathExpression response) {
        if (promptType == PromptType.SET && response instanceof ISet set) {
//            String raw = modifier.getInformation().toString();
//
//            Map<String, Object> context = Map.of(TemplatePrefix.SET.getKey(), set);
//
//            String resolved = templateResolver.resolve(raw, context);
//            System.out.println(resolved);
            return true;
        }

        throw new PromptValidatorException("Unsupported PromptType: " + promptType);
    }
}