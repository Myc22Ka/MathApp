package pl.myc22ka.mathapp.exercise.variant.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.exercise.variant.repository.TemplateExerciseVariantRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TemplateExerciseVariantService {

    private final TemplateExerciseRepository exerciseRepository;
    private final TemplateExerciseVariantRepository variantRepository;

    // CREATE
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

    // READ ALL for one template
    public List<TemplateExerciseVariant> getVariantsByExercise(Long exerciseId) {
        return variantRepository.findByTemplateExerciseId(exerciseId);
    }

    // READ ONE
    public TemplateExerciseVariant getById(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Variant not found with id " + id));
    }

    // UPDATE
    public TemplateExerciseVariant update(Long id, @NotNull TemplateExerciseVariantRequest request) {
        TemplateExerciseVariant existing = getById(id);

        existing.setDifficulty(request.difficulty());
        existing.setText(request.text());
        existing.setAnswer(request.answer());

        return variantRepository.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        variantRepository.deleteById(id);
    }
}