package pl.myc22ka.mathapp.exercise.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exercise.template.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.template.service.TemplateExerciseVariantService;

@RestController
@RequestMapping("/api/template-exercises")
@RequiredArgsConstructor
public class TemplateExerciseVariantController {

    private final TemplateExerciseVariantService variantService;

    @PostMapping("/{exerciseId}/variants")
    public ResponseEntity<TemplateExerciseVariant> createVariant(
            @PathVariable Long exerciseId,
            @RequestBody TemplateExerciseVariantRequest request) {

        TemplateExerciseVariant created = variantService.createVariant(exerciseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}