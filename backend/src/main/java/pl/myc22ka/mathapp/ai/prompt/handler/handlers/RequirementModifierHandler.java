package pl.myc22ka.mathapp.ai.prompt.handler.handlers;

import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.handler.ModifierHandler;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Requirement;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.RequirementModifier;

@Component
public class RequirementModifierHandler implements ModifierHandler<RequirementModifier> {

    @Override
    public boolean supports(Modifier modifier) {
        return modifier instanceof RequirementModifier;
    }

    @Override
    public boolean apply(RequirementModifier modifier, PromptType promptType, String resposne) {
        Requirement req = modifier.getRequirement();
        switch (req) {
            case INTERVALS -> System.out.println("Użyj tylko notacji przedziałowej");
            case UNION -> System.out.println("Dodaj wsparcie dla sumy zbiorów");
            case DIFFERENCE -> System.out.println("Dodaj wsparcie dla różnicy zbiorów");
            case TRIGONOMETRY -> {
                if (promptType == PromptType.FUNCTION) {
                    System.out.println("Dodaj funkcje trygonometryczne");
                } else {
                    System.out.println("TRIGONOMETRY nie ma sensu dla zbiorów");
                }
            }
            case SYMJASUPPORT -> System.out.println("Włącz parser Symja");
        }

        return true;
    }
}
