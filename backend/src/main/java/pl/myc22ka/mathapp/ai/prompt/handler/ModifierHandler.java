package pl.myc22ka.mathapp.ai.prompt.handler;

import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;

public interface ModifierHandler<T extends Modifier> {
    boolean supports(Modifier modifier);
    boolean apply(T modifier, PromptType promptType);
}