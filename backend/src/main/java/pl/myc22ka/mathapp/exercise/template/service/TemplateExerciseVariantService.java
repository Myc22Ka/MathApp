package pl.myc22ka.mathapp.exercise.template.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.exercise.template.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseVariantRepository;

@Service
@RequiredArgsConstructor
public class TemplateExerciseVariantService {

    private final TemplateExerciseRepository exerciseRepository;
    private final TemplateExerciseVariantRepository variantRepository;

    public TemplateExerciseVariant createVariant(Long exerciseId, @NotNull TemplateExerciseVariantRequest request) {
        TemplateExercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("TemplateExercise not found with id: " + exerciseId));

        TemplateExerciseVariant variant = TemplateExerciseVariant.builder()
                .templateExercise(exercise)
                .difficulty(request.difficulty())
                .text(request.text())
                .answer(request.answer())
                .build();

        return variantRepository.save(variant);
    }
}