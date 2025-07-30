package pl.myc22ka.mathapp.ai.prompt.handler.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.handler.ModifierHandler;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.ai.prompt.service.ModifierService;

@Component
@RequiredArgsConstructor
public class DifficultyModifierHandler implements ModifierHandler<DifficultyModifier> {

    private final ModifierService modifierService;

    @Override
    public boolean supports(Modifier modifier) {
        return modifier instanceof DifficultyModifier;
    }

    @Override
    public boolean apply(DifficultyModifier modifier, PromptType promptType) {
        boolean verified = true;

        if (promptType == PromptType.SET) {
            int maxLevel = modifierService.getMaxDifficultyLevel(modifier.getTopic());

            if (modifier.getDifficultyLevel() > maxLevel) {
                System.out.println("Poziom trudności przekracza maksymalny dla tego tematu");
                return false;
            }

            switch (modifier.getDifficultyLevel()) {
                case 1 -> System.out.println("Poziom 1: Tylko podstawowe przedziały.");
                case 2 -> System.out.println("Poziom 2: Przedziały + zbiory skończone.");
                case 3 -> System.out.println("Poziom 3: Przedziały, zbiory skończone, ℝ i ∅.");
                case 4 -> System.out.println("Poziom 4: Logika liczb rzeczywistych + symbole matematyczne.");
                default -> {
                    System.out.println("Nieznany poziom trudności.");
                    verified = false;
                }
            }
        }

        else if (promptType == PromptType.FUNCTION) {
            System.out.println("Funkcja ignoruje poziom trudności");

            verified = false;
        }

        else {
            System.out.println("Nieobsługiwany PromptType: " + promptType);
            verified = false;
        }

        return verified;
    }
}
