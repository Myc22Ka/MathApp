package pl.myc22ka.mathapp.exercise.variant.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.exceptions.custom.TemplateAlreadyExistsException;
import pl.myc22ka.mathapp.exceptions.custom.VariantTextMismatch;
import pl.myc22ka.mathapp.exercise.template.model.Step;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.repository.TemplateExerciseVariantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class VariantExerciseHelper {

    private final TemplateExerciseVariantRepository templateVariantRepository;
    private final TemplateResolver templateResolver;

    public TemplateExerciseVariant getVariant(Long variantId) {
        return templateVariantRepository.findById(variantId)
                .orElseThrow(() -> new IllegalArgumentException("Variant not found with id " + variantId));
    }

    public void prepareForCreate(@NotNull TemplateExerciseVariant template) {
        String cleanText = templateResolver.removeTemplatePlaceholders(template.getTemplateText());

        template.setClearText(cleanText);
        template.setExerciseCounter(0L);
    }

    public void validateUnique(@NotNull TemplateExerciseVariant template) {
        String text = template.getTemplateText();
        List<TemplateExerciseVariant> allTemplates = templateVariantRepository.findAll();

        var modifiers = templateResolver.findPrefixModifiers(text);

        for (TemplateExerciseVariant t : allTemplates) {
            var templateText = t.getTemplateText();

            if (templateText.equals(text)) {
                throw new TemplateAlreadyExistsException(
                        "Variant already exists"
                );
            }

            var templateModifiers = templateResolver.findPrefixModifiers(templateText);

            if (PrefixModifierEntry.areEqualLists(modifiers, templateModifiers)) {
                throw new TemplateAlreadyExistsException("Variant already exists with the same modifiers");
            }
        }
    }

    public void validateCleanTextVariant(@NotNull TemplateExerciseVariant variant, String templateText2){
        String cleanText1 = variant.getClearText();
        String cleanText2 = templateResolver.removeTemplatePlaceholders(templateText2);

        if(cleanText1.equals(cleanText2)){
            throw new VariantTextMismatch("Variant text shouldn't be changed");
        };
    }

    public void applyHardUpdateVariant(@NotNull TemplateExerciseVariant existing,
                                       @NotNull TemplateExerciseVariantRequest request) {
        existing.getSteps().clear();

        existing.setExerciseCounter(0L);
        existing.setDifficulty(request.difficulty());
        existing.setTemplateText(request.text());
        existing.setTemplateAnswer(request.answer());
        existing.setClearText(request.text());

        if (request.steps() != null) {
            request.steps().forEach(step -> step.setVariant(existing));
            existing.getSteps().addAll(request.steps());
        }
    }
}
