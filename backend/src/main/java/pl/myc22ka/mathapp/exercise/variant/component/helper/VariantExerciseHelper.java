package pl.myc22ka.mathapp.exercise.variant.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.exceptions.custom.TemplateAlreadyExistsException;
import pl.myc22ka.mathapp.exceptions.custom.VariantTextMismatch;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.repository.TemplateExerciseVariantRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VariantExerciseHelper {

    private final TemplateExerciseVariantRepository templateVariantRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
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

    public void validateUnique(@NotNull TemplateExerciseVariant variant) {
        String text = variant.getTemplateText();
        var modifiers = templateResolver.findPrefixModifiers(text);

        List<TemplateExerciseVariant> variantsForTemplate =
                templateVariantRepository.findByTemplateExerciseId(variant.getTemplateExercise().getId());

        for (TemplateExerciseVariant t : variantsForTemplate) {
            if (t.getTemplateText().equals(text)) {
                throw new TemplateAlreadyExistsException("Variant already exists");
            }

            var templateModifiers = templateResolver.findPrefixModifiers(t.getTemplateText());
            if (PrefixModifierEntry.areEqualLists(modifiers, templateModifiers)) {
                throw new TemplateAlreadyExistsException("Variant already exists with the same modifiers");
            }
        }

        List<TemplateExercise> templates = templateExerciseRepository.findAll();

        for (TemplateExercise t : templates) {
            if (t.getTemplateText().equals(text)) {
                throw new TemplateAlreadyExistsException("A template already exists with the same text");
            }

            var templateModifiers = templateResolver.findPrefixModifiers(t.getTemplateText());
            if (PrefixModifierEntry.areEqualLists(modifiers, templateModifiers)) {
                throw new TemplateAlreadyExistsException("A template already exists with the same modifiers");
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
        existing.setTemplateText(request.templateText());
        existing.setTemplateAnswer(request.templateAnswer());

        String cleanText = templateResolver.removeTemplatePlaceholders(request.templateText());
        existing.setClearText(cleanText);

        if (request.steps() != null) {
            existing.getSteps().addAll(
                    request.steps().stream()
                            .map(stepDto -> stepDto.toEntityForVariant(existing))
                            .toList()
            );
        }
    }
}
