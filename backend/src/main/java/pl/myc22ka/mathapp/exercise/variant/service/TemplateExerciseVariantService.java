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

@Service
@RequiredArgsConstructor
public class TemplateExerciseVariantService {

    private final VariantExerciseHelper variantExerciseHelper;
    private final TemplateExerciseVariantRepository variantRepository;
    private final TemplateExerciseHelper templateExerciseHelper;

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

    public List<TemplateExerciseVariant> getAll() {
        return variantRepository.findAll();
    }

    public TemplateExerciseVariant getById(Long id) {
        return variantExerciseHelper.getVariant(id);
    }

    @Transactional
    public TemplateExerciseVariant update(Long id, @NotNull TemplateExerciseVariantRequest request) {
        TemplateExerciseVariant existing = variantExerciseHelper.getVariant(id);

        variantExerciseHelper.validateCleanTextVariant(existing, request.templateText());
        variantExerciseHelper.applyHardUpdateVariant(existing, request);

        return variantRepository.save(existing);
    }

    public void delete(Long id) {
        variantRepository.deleteById(id);
    }
}