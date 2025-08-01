package pl.myc22ka.mathapp.ai.prompt.handler.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.handler.ModifierHandler;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Requirement;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.RequirementModifier;
import pl.myc22ka.mathapp.exceptions.custom.PromptValidatorException;

@Component
@RequiredArgsConstructor
public class RequirementModifierHandler implements ModifierHandler<RequirementModifier> {

    @Override
    public boolean supports(Modifier modifier) {
        return modifier instanceof RequirementModifier;
    }

    @Override
    public boolean apply(RequirementModifier modifier, PromptType promptType, String response) {
        Requirement req = modifier.getRequirement();

        switch (req) {
            case INTERVALS -> {
                System.out.println("Użyj tylko notacji przedziałowej (np. [1, 5))");
                // Możesz dodać np. walidację, czy odpowiedź zawiera nawiasy itd.
            }

            case UNION -> {
                if (!response.contains("∪")) {
                    throw new PromptValidatorException("Odpowiedź musi zawierać sumę zbiorów (∪).");
                }
            }

            case DIFFERENCE -> {
                if (!response.contains("∖")) {
                    throw new PromptValidatorException("Odpowiedź musi zawierać różnicę zbiorów (∖).");
                }
            }

            case TRIGONOMETRY -> {
                if (promptType == PromptType.FUNCTION) {
                    if (!containsTrigFunction(response)) {
                        throw new PromptValidatorException("Dodaj funkcję trygonometryczną (sin, cos, tan...)");
                    }
                } else {
                    throw new PromptValidatorException("TRIGONOMETRY nie ma sensu dla zbiorów.");
                }
            }

            case SYMJASUPPORT -> {
                System.out.println("Włącz parser Symja – ta odpowiedź powinna być zgodna ze składnią Symja.");
                // Można tu np. sprawdzić, czy Symja potrafi sparsować odpowiedź
            }

            default -> throw new PromptValidatorException("Nieznany requirement: " + req);
        }

        return true; // jeśli nie rzuci wyjątku – przechodzi walidację
    }

    private boolean containsTrigFunction(String response) {
        return response.contains("sin") || response.contains("cos") || response.contains("tan");
    }
}