package pl.myc22ka.mathapp.exercise.variant.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.exercise.template.component.helper.TemplateExerciseHelper;
import pl.myc22ka.mathapp.exercise.variant.component.helper.VariantExerciseHelper;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.repository.TemplateExerciseVariantRepository;

import java.util.List;

/**
 * Service layer for managing {@link TemplateExerciseVariant} entities.
 * Handles creation, retrieval, update, and deletion of template exercise variants.
 * Delegates validation and preparation logic to {@link VariantExerciseHelper}.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Service
@RequiredArgsConstructor
public class TemplateExerciseVariantService {

    private final VariantExerciseHelper variantExerciseHelper;
    private final TemplateExerciseVariantRepository variantRepository;
    private final TemplateExerciseHelper templateExerciseHelper;

    /**
     * Creates a new template exercise variant for a given template.
     *
     * @param templateId the ID of the parent template exercise
     * @param request    data for the new variant
     * @return the created variant
     */
    @Transactional
    public TemplateExerciseVariant create(Long templateId, @NotNull TemplateExerciseVariantRequest request) {
        TemplateExercise template = templateExerciseHelper.getTemplate(templateId);

        TemplateExerciseVariant variant = TemplateExerciseVariant.builder()
                .templateExercise(template)
                .category(template.getCategory())
                .difficulty(request.difficulty())
                .templateText(request.templateText())
                .templateAnswer(request.templateAnswer())
                .build();

        if (request.steps() != null) {
            variant.getSteps().addAll(
                    request.steps().stream()
                            .map(stepDto -> stepDto.toEntityForVariant(variant))
                            .toList()
            );
        }

        variantExerciseHelper.validateUnique(variant);
        variantExerciseHelper.prepareForCreate(variant);

        return variantRepository.save(variant);
    }

    /**
     * Retrieves all template exercise variants.
     *
     * @return list of all variants
     */
    public List<TemplateExerciseVariant> getAll() {
        return variantRepository.findAll();
    }

    /**
     * Retrieves a variant by its ID.
     *
     * @param id the variant ID
     * @return the variant entity
     * @throws IllegalArgumentException if not found
     */
    public TemplateExerciseVariant getById(Long id) {
        return variantExerciseHelper.getVariant(id);
    }

    /**
     * Updates an existing template exercise variant.
     * Validates that the clean text is not changed, then applies a hard update.
     *
     * @param id      the variant ID
     * @param request the updated variant data
     * @return the updated variant
     */
    @Transactional
    public TemplateExerciseVariant update(Long id, @NotNull TemplateExerciseVariantRequest request) {
        TemplateExerciseVariant existing = variantExerciseHelper.getVariant(id);

        variantExerciseHelper.validateCleanTextVariant(existing, request.templateText());
        variantExerciseHelper.applyHardUpdateVariant(existing, request);

        return variantRepository.save(existing);
    }

    /**
     * Deletes a variant by its ID.
     *
     * @param id the variant ID
     */
    public void delete(Long id) {
        variantRepository.deleteById(id);
    }
}