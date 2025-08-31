package pl.myc22ka.mathapp.exercise.template.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exercise.template.dto.TemplateExerciseDTO;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.service.TemplateExerciseService;

@RestController
@RequestMapping("/api/template-exercises")
@RequiredArgsConstructor
public class TemplateExerciseController {

    private final TemplateExerciseService service;

    @PostMapping
    public ResponseEntity<TemplateExerciseDTO> createTemplate(@NotNull @RequestBody TemplateExerciseDTO dto) {
        TemplateExercise saved = service.create(dto.toEntity());
        return ResponseEntity.ok(TemplateExerciseDTO.fromEntity(saved));
    }
}

