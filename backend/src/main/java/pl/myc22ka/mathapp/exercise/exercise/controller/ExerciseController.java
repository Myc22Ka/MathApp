package pl.myc22ka.mathapp.exercise.exercise.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exceptions.DefaultResponse;
import pl.myc22ka.mathapp.exercise.exercise.annotation.rating.Rating;
import pl.myc22ka.mathapp.exercise.exercise.component.ExerciseScheduler;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseDTO;
import pl.myc22ka.mathapp.exercise.exercise.service.ExerciseService;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.time.Instant;
import java.util.List;

/**
 * REST controller for managing math exercises.
 * <p>
 * Provides endpoints for creating, generating, retrieving and deleting exercises
 * based on predefined templates.
 *
 * @author Myc22Ka
 * @version 1.0.3
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
     * Retrieves a paginated list of exercises with optional filtering and sorting.
     *
     * @param page          Zero-based page index (default: 0)
     * @param size          Number of items per page (default: 20)
     * @param category      Optional filter by category
     * @param type          Optional filter by exercise type
     * @param sortBy        Field name used for sorting (default: id)
     * @param sortDirection Sorting direction: "asc" or "desc" (default: asc)
     * @return Paginated list of exercises matching the criteria
     * TODO: Change comment
     */
    @Operation(
            summary = "Get exercises",
            description = "Returns a paginated list of exercises with optional filters, sorting and pagination parameters."
    )
    @GetMapping
    public Page<ExerciseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filter by rating", example = "1.0")
            @RequestParam(required = false) Double rating,
            @Parameter(description = "Filter by difficulty level", example = "1")
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) TemplatePrefix category,
            @RequestParam(required = false) Long templateId,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        return exerciseService.getAll(page, size, rating, difficulty, category, sortBy, sortDirection, templateId);
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
     * Rates an exercise with a given rating (1 - 5, step 0.5).
     */
    @Operation(
            summary = "Rate exercise",
            description = "Sets a rating (1 - 5, step 0.5) for a given exercise."
    )
    @PostMapping("/{id}/rate")
    public ResponseEntity<DefaultResponse> rateExercise(
            @PathVariable Long id,
            @RequestParam @Rating Double rating) {
        exerciseService.rateExercise(id, rating);

        return ResponseEntity.ok(
                new DefaultResponse(
                        Instant.now().toString(),
                        "Rating set successfully",
                        200
                )
        );
    }

    /**
     * Deletes an exercise by its ID.
     */
    @Operation(
            summary = "Delete exercise",
            description = "Deletes the exercise with the given ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResponse> delete(@PathVariable Long id) {
        exerciseService.delete(id);

        return ResponseEntity.ok(
                new DefaultResponse(
                        Instant.now().toString(),
                        "Exercise deleted successfully",
                        200
                )
        );
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

    @PostMapping("/solve/{exerciseId}")
    @Operation(
            summary = "Solve exercise",
            description = "Executes all steps of the exercise and returns whether the answer is correct."
    )
    public ResponseEntity<DefaultResponse> solve(
            @PathVariable Long exerciseId,
            @RequestParam String userAnswer) {

        boolean correct = exerciseService.solve(exerciseId, userAnswer);

        return ResponseEntity.ok(
                new DefaultResponse(
                        java.time.Instant.now().toString(),
                        correct ? "Answer is correct" : "Answer is incorrect",
                        correct ? 200 : 400
                )
        );
    }
}
