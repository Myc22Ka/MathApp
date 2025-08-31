package pl.myc22ka.mathapp.exercise.template.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exercise.template.dto.TemplateExerciseDTO;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.service.TemplateExerciseService;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<TemplateExerciseDTO>> getAllTemplates() {
        List<TemplateExerciseDTO> templates = service.getAll().stream()
                .map(TemplateExerciseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateExerciseDTO> getTemplateById(@PathVariable Long id) {
        TemplateExercise template = service.getById(id);
        return ResponseEntity.ok(TemplateExerciseDTO.fromEntity(template));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateExerciseDTO> updateTemplate(
            @PathVariable Long id,
            @NotNull @RequestBody TemplateExerciseDTO dto
    ) {
        TemplateExercise updated = service.update(id, dto.toEntity());
        return ResponseEntity.ok(TemplateExerciseDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

