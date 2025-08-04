package pl.myc22ka.mathapp.ai.prompt.handler.handlers;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.handler.ModifierHandler;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.exceptions.custom.PromptValidatorException;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.set.ISet;

import static pl.myc22ka.mathapp.ai.prompt.model.PromptType.SET;

@Component
@RequiredArgsConstructor
public class TemplateModifierHandler implements ModifierHandler<TemplateModifier> {

    @Override
    public boolean supports(Modifier modifier) {
        return modifier instanceof TemplateModifier;
    }

    @Override
    public boolean apply(TemplateModifier modifier, PromptType promptType, MathExpression response) {
        if (promptType == SET && response instanceof ISet set) {
            return validateSets(modifier, set);
        }

        throw new PromptValidatorException("Unsupported PromptType: " + promptType);
    }

    private boolean validateSets(@NotNull TemplateModifier modifier, ISet set) {

        if(modifier.getInformation() instanceof ISet other) {


            return switch (modifier.getTemplate()) {
                case DISJOINT_SETS -> set.areDisjoint(other);
            };
        }

        return false;
    }
}