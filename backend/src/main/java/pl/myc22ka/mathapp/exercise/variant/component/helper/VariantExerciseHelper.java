package pl.myc22ka.mathapp.exercise.variant.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.utils.resolver.component.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.exceptions.custom.TemplateAlreadyExistsException;
import pl.myc22ka.mathapp.exceptions.custom.VariantTextMismatch;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.repository.TemplateExerciseVariantRepository;
import pl.myc22ka.mathapp.step.dto.StepDTO;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.model.StepWrapper;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;

import java.util.List;

/**
 * Helper component for handling operations on {@link TemplateExerciseVariant}.
 * Encapsulates validation, preparation, and update logic separate from the service layer.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Component
@RequiredArgsConstructor
public class VariantExerciseHelper {

    private final TemplateExerciseVariantRepository templateVariantRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final TemplateResolver templateResolver;
    private final StepDefinitionRepository stepDefinitionRepository;

    /**
     * Retrieves a variant by its id.
     *
     * @param variantId the id of the variant
     * @return the found variant
     * @throws IllegalArgumentException if the variant is not found
     */
    public TemplateExerciseVariant getVariant(Long variantId) {
        return templateVariantRepository.findByIdWithTemplate(variantId)
                .orElseThrow(() -> new IllegalArgumentException("Variant not found with id " + variantId));
    }

    /**
     * Prepares a new variant for creation by removing placeholders
     * and initializing default values.
     *
     * @param template the variant to prepare
     */
    public void prepareForCreate(@NotNull TemplateExerciseVariant template) {
        String cleanText = templateResolver.removeTemplatePlaceholders(template.getTemplateText());

        template.setClearText(cleanText);
        template.setExerciseCounter(0L);
    }

    /**
     * Validates that the given variant is unique across both
     * variants of the same template and all templates in the system.
     * <p>
     * Checks uniqueness based on:
     * <ul>
     *   <li>Exact template text</li>
     *   <li>Prefix modifiers extracted from the template</li>
     * </ul>
     *
     * @param variant the variant to validate
     * @throws TemplateAlreadyExistsException if a duplicate is found
     */
    public void validateUnique(@NotNull TemplateExerciseVariant variant) {
        String text = variant.getTemplateText();
        var modifiers = templateResolver.findPrefixModifiers(text);

        List<TemplateExerciseVariant> variantsForTemplate =
                templateVariantRepository.findByTemplateExerciseId(variant.getTemplateExercise().getId());

        for (TemplateExerciseVariant t : variantsForTemplate) {
            if (!t.getTemplateText().equals(text)) continue;

            var templateModifiers = templateResolver.findPrefixModifiers(t.getTemplateText());
            if (PrefixModifierEntry.areEqualLists(modifiers, templateModifiers)) {
                throw new TemplateAlreadyExistsException("Variant already exists with the same text and modifiers");
            }
        }

        List<TemplateExercise> templates = templateExerciseRepository.findAll();

        for (TemplateExercise t : templates) {
            if (!t.getTemplateText().equals(text)) continue;

            var templateModifiers = templateResolver.findPrefixModifiers(t.getTemplateText());
            if (PrefixModifierEntry.areEqualLists(modifiers, templateModifiers)) {
                throw new TemplateAlreadyExistsException("A template already exists with the same text and modifiers");
            }
        }
    }

    /**
     * Ensures that the clean text of the variant does not change
     * when updating the template text.
     *
     * @param variant       the existing variant
     * @param templateText2 the new template text
     * @throws VariantTextMismatch if the clean text remains the same (i.e., text hasn’t really changed)
     */
    public void validateCleanTextVariant(@NotNull TemplateExerciseVariant variant, String templateText2) {
        String cleanText1 = variant.getClearText();
        String cleanText2 = templateResolver.removeTemplatePlaceholders(templateText2);

        if (cleanText1.equals(cleanText2)) {
            throw new VariantTextMismatch("Variant text shouldn't be changed");
        }
    }

    /**
     * Performs a hard update on an existing variant using the given request.
     * Replaces all fields and steps with the new values.
     *
     * @param existing the variant to update
     * @param request  the update request containing new values
     */
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
            existing.getSteps().addAll(createStepWrappers(request.steps(), existing));
        }
    }

    /**
     * Creates a list of StepWrapper instances from step DTOs.
     * This method centralizes the logic for converting step definitions
     * into StepWrapper objects, eliminating code duplication.
     *
     * @param stepDtos the list of step DTOs
     * @param variant  the variant to associate with the steps
     * @return list of StepWrapper instances
     * @throws IllegalArgumentException if a step definition is not found
     */
    public List<StepWrapper> createStepWrappers(@NotNull List<StepDTO> stepDtos,
                                                @NotNull TemplateExerciseVariant variant) {
        return stepDtos.stream()
                .map(stepDto -> {
                    StepDefinition def = stepDefinitionRepository.findById(stepDto.stepDefinitionId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Step definition not found with id " + stepDto.stepDefinitionId()));

                    return StepWrapper.builder()
                            .stepDefinition(def)
                            .variant(variant)
                            .prefixes(stepDto.prefixes())
                            .build();
                })
                .toList();
    }
}
