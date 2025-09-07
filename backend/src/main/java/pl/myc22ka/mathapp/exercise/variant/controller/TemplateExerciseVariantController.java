package pl.myc22ka.mathapp.exercise.variant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exceptions.DefaultResponse;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantRequest;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.service.TemplateExerciseVariantService;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/template-exercises/variants")
@RequiredArgsConstructor
@Tag(name = "Template Exercise Variants", description = "Operations for managing template exercise variants")
public class TemplateExerciseVariantController {

    private final TemplateExerciseVariantService variantService;

    /**
     * Creates a new variant for a specific template exercise.
     */
    @PostMapping
    @Operation(
            summary = "Create a new template exercise variant",
            description = "Creates a new variant for the specified template exercise and returns a success message."
    )
    public ResponseEntity<DefaultResponse> create(
            @PathVariable Long templateId,
            @NotNull @RequestBody TemplateExerciseVariantRequest request
    ) {
        variantService.create(templateId, request);

        return ResponseEntity.ok(new DefaultResponse(
                Instant.now().toString(),
                "Template exercise variant created successfully",
                200
        ));
    }

    /**
     * Retrieves all variants for all templates.
     */
    @GetMapping
    @Operation(
            summary = "Get all template exercise variants",
            description = "Returns a list of all template exercise variants in the system."
    )
    public ResponseEntity<List<TemplateExerciseVariant>> getAll() {
        return ResponseEntity.ok(variantService.getAll());
    }

    /**
     * Retrieves a specific variant by its ID.
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a template exercise variant by ID",
            description = "Returns the variant with the given ID."
    )
    public ResponseEntity<TemplateExerciseVariant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.getById(id));
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
    public ResponseEntity<DefaultResponse> update(
            @PathVariable Long id,
            @NotNull @RequestBody TemplateExerciseVariantRequest request
    ) {
        variantService.update(id, request);

        return ResponseEntity.ok(new DefaultResponse(
                Instant.now().toString(),
                "Template exercise variant updated successfully",
                200
        ));
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