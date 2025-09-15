package pl.myc22ka.mathapp.exercise.exercise.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exercise.exercise.component.ExerciseScheduler;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseDTO;
import pl.myc22ka.mathapp.exercise.exercise.service.ExerciseService;

import java.util.List;

/**
 * REST controller for managing math exercises.
 * <p>
 * Provides endpoints for creating, generating, retrieving and deleting exercises
 * based on predefined templates.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 31.08.2025
 */
@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Tag(
        name = "Exercises",
        description = "API endpoints for creating, generating, retrieving and deleting math exercises"
)
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseScheduler exerciseScheduler;

    /**
     * Creates a new exercise based on user-provided values.
     */
    @Operation(
            summary = "Create exercise",
            description = "Creates a new exercise using the selected template and user-provided values."
    )
    @PostMapping("/create/{templateId}")
    public ResponseEntity<ExerciseDTO> create(
            @RequestParam(required = false) Long templateId,
            @RequestParam(required = false) Long variantId,
            @RequestBody List<String> values
    ) {
        return ResponseEntity.ok(ExerciseDTO.fromEntity(exerciseService.create(templateId, variantId, values)));
    }

    /**
     * Automatically generates a new exercise using AI.
     */
    @Operation(
            summary = "Generate exercise",
            description = "Generates a new exercise with AI based on the selected template."
    )
    @PostMapping("/generate/{templateId}")
    public ResponseEntity<ExerciseDTO> generate(
            @RequestParam(required = false) Long templateId,
            @RequestParam(required = false) Long variantId
    ) {
        return ResponseEntity.ok(ExerciseDTO.fromEntity(exerciseService.generate(templateId, variantId)));
    }

    /**
     * Retrieves a specific exercise by its ID.
     */
    @Operation(
            summary = "Get exercise by ID",
            description = "Returns the exercise with the given ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(ExerciseDTO.fromEntity(exerciseService.getById(id)));
    }

    /**
     * Retrieves all exercises.
     */
    @Operation(
            summary = "Get all exercises",
            description = "Returns a list of all exercises."
    )
    @GetMapping
    public ResponseEntity<List<ExerciseDTO>> getAll() {
        List<ExerciseDTO> exercises = exerciseService.getAll().stream()
                .map(ExerciseDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(exercises);
    }

    /**
     * Retrieves a random exercise from the database.
     */
    @Operation(
            summary = "Get random exercise",
            description = "Returns a random exercise from the database."
    )
    @GetMapping("/random")
    public ResponseEntity<ExerciseDTO> getRandom() {
        return ResponseEntity.ok(ExerciseDTO.fromEntity(exerciseScheduler.getLastRandomExercise()));
    }

    /**
     * Deletes an exercise by its ID.
     */
    @Operation(
            summary = "Delete exercise",
            description = "Deletes the exercise with the given ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exerciseService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Updates values of an existing exercise.
     */
    @Operation(
            summary = "Update exercise values",
            description = "Updates an existing exercise with new values."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> update(
            @PathVariable Long id,
            @RequestBody List<String> values
    ) {
        return ResponseEntity.ok(ExerciseDTO.fromEntity(exerciseService.update(id, values)));
    }

    @Operation(
            summary = "Solve exercise",
            description = "Executes all steps of the exercise and returns the final answer."
    )
    @PostMapping("/solve/{id}")
    public ResponseEntity<ExerciseDTO> solve(@PathVariable Long id) {
        return ResponseEntity.ok(ExerciseDTO.fromEntity(exerciseService.solve(id)));
    }
}
