package pl.myc22ka.mathapp.exercise.template.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exceptions.DefaultResponse;
import pl.myc22ka.mathapp.exercise.template.dto.TemplateExerciseDTO;
import pl.myc22ka.mathapp.exercise.template.service.TemplateExerciseService;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing TemplateExercise entities.
 * Provides endpoints for CRUD operations on template exercises.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@RestController
@RequestMapping("/api/template-exercises")
@RequiredArgsConstructor
@Tag(name = "Template Exercises", description = "Operations for managing template exercises")
public class TemplateExerciseController {

    private final TemplateExerciseService service;

    /**
     * Creates a new template exercise in the database.
     */
    @PostMapping
    @Operation(
            summary = "Create a new template exercise",
            description = "Creates a new template exercise and returns a success or error message."
    )
    public ResponseEntity<DefaultResponse> create(@NotNull @RequestBody TemplateExerciseDTO dto) {
        service.create(dto.toEntity());

        return ResponseEntity.ok(new DefaultResponse(
                Instant.now().toString(),
                "Template exercise created successfully",
                200
        ));
    }

    /**
     * Retrieves all template exercises.
     */
    @GetMapping
    @Operation(
            summary = "Get all template exercises",
            description = "Returns a list of all template exercises in the system."
    )
    public ResponseEntity<List<TemplateExerciseDTO>> getAll() {
        List<TemplateExerciseDTO> templates = service.getAll().stream()
                .map(TemplateExerciseDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(templates);
    }

    /**
     * Retrieves a specific template exercise by its ID.
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a template exercise by ID",
            description = "Returns the template exercise with the given ID."
    )
    public ResponseEntity<TemplateExerciseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(TemplateExerciseDTO.fromEntity(service.getById(id)));
    }

    /**
     * Updates an existing template exercise.
     * Soft update changes only clearText, hard update overwrites everything
     * and removes all associated exercises, steps and variants.
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update a template exercise",
            description = "Updates an existing template exercise. Soft update modifies only clearText; hard update overwrites everything and deletes all related exercises, steps, and variants."
    )
    public ResponseEntity<DefaultResponse> update(
            @PathVariable Long id,
            @NotNull @RequestBody TemplateExerciseDTO dto
    ) {
        service.update(id, dto.toEntity());

        return ResponseEntity.ok(new DefaultResponse(
                Instant.now().toString(),
                "Template exercise updated successfully",
                200
        ));
    }

    /**
     * Deletes a template exercise by ID.
     * Cascade deletes all related steps, exercises and variants.
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a template exercise",
            description = "Deletes a template exercise by its ID, including all related steps, exercises, and variants via cascade."
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}