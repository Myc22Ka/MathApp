package pl.myc22ka.mathapp.exercise.variant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.service.TemplateExerciseVariantService;

import java.util.List;

@RestController
@RequestMapping("/api/template-exercises/{exerciseId}/variants")
@RequiredArgsConstructor
public class TemplateExerciseVariantController {

    private final TemplateExerciseVariantService variantService;

    @PostMapping
    public ResponseEntity<TemplateExerciseVariant> create(
            @PathVariable Long exerciseId,
            @RequestBody TemplateExerciseVariantRequest request
    ) {
        return ResponseEntity.ok(variantService.createVariant(exerciseId, request));
    }

    @GetMapping
    public ResponseEntity<List<TemplateExerciseVariant>> getAll(@PathVariable Long exerciseId) {
        return ResponseEntity.ok(variantService.getVariantsByExercise(exerciseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateExerciseVariant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateExerciseVariant> update(
            @PathVariable Long id,
            @RequestBody TemplateExerciseVariantRequest request
    ) {
        return ResponseEntity.ok(variantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        variantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}