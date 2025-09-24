package pl.myc22ka.mathapp.exercise.variant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantResponse;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.service.TemplateExerciseVariantService;

/**
 * REST controller for managing template exercise variants.
 * Provides CRUD operations (create, read, update, delete).
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@RestController
@RequestMapping("/api/template-exercises/variants")
@RequiredArgsConstructor
@Tag(name = "Template Exercise Variants", description = "Operations for managing template exercise variants")
public class TemplateExerciseVariantController {

    private final TemplateExerciseVariantService variantService;

    /**
     * Creates a new variant for a specific template ID exercise.
     */
    @PostMapping("/{id}")
    @Operation(
            summary = "Create a new template exercise variant",
            description = "Creates a new variant for the specified template ID exercise and returns the created variant."
    )
    public ResponseEntity<TemplateExerciseVariantResponse> create(
            @PathVariable Long id,
            @NotNull @RequestBody TemplateExerciseVariantRequest request
    ) {
        TemplateExerciseVariant created = variantService.create(id, request);
        return ResponseEntity.ok(TemplateExerciseVariantResponse.fromEntity(created));
    }

    /**
     * Retrieves all variants for all templates.
     */
    @Operation(
            summary = "Get template exercise variants",
            description = "Returns a paginated list of template exercise variants with optional filters and sorting."
    )
    @GetMapping
    public ResponseEntity<Page<TemplateExerciseVariantResponse>> getAllVariants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) PromptType category,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<TemplateExerciseVariantResponse> variants =
                variantService.getAll(page, size, difficulty, category, sortBy, sortDirection);

        return ResponseEntity.ok(variants);
    }

    /**
     * Retrieves a specific variant by its ID.
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a template exercise variant by ID",
            description = "Returns the variant with the given ID."
    )
    public ResponseEntity<TemplateExerciseVariantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                TemplateExerciseVariantResponse.fromEntity(variantService.getById(id))
        );
    }

    /**
     * Updates an existing variant.
     * Hard update removes all steps and exercises associated with the variant.
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update a template exercise variant",
            description = "Updates an existing variant. Hard update removes all steps and exercises associated with the variant."
    )
    public ResponseEntity<TemplateExerciseVariantResponse> update(
            @PathVariable Long id,
            @NotNull @RequestBody TemplateExerciseVariantRequest request
    ) {
        TemplateExerciseVariant updated = variantService.update(id, request);
        return ResponseEntity.ok(TemplateExerciseVariantResponse.fromEntity(updated));
    }

    /**
     * Deletes a variant by ID.
     * Cascade deletes all steps and exercises associated with the variant.
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a template exercise variant",
            description = "Deletes a variant by its ID, including all associated steps and exercises via cascade."
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        variantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}