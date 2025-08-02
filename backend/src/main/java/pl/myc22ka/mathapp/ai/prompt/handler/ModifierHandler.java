package pl.myc22ka.mathapp.ai.prompt.handler;

import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.model.expression.MathExpression;

public interface ModifierHandler<T extends Modifier> {
    boolean supports(Modifier modifier);
    boolean apply(T modifier, PromptType promptType, MathExpression response);
}