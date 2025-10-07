package pl.myc22ka.mathapp.ai.prompt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierDTO;
import pl.myc22ka.mathapp.ai.prompt.service.ModifierService;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Controller responsible for listing Modifiers.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @see ModifierDTO
 * @see TemplatePrefix
 * @see ModifierService
 * @since 01.10.2025
 */
@RestController
@RequestMapping("/api/modifiers")
@RequiredArgsConstructor
@Tag(name = "Modifiers", description = "Endpoints for managing modifiers")
public class ModifierController {

    private final ModifierService modifierService;

    /**
     * Get a paginated list of modifiers with optional filtering and sorting.
     *
     * @param page          page number (0-based)
     * @param size          page size
     * @param category      optional filter by PromptType category
     * @param sortBy        field to sort by (default: id)
     * @param sortDirection sort direction ASC/DESC (default: ASC)
     * @return a page of ModifierDTO
     */
    @Operation(summary = "Get all modifiers", description = "Retrieve modifiers with pagination, sorting and optional category filter")
    @GetMapping
    public Page<ModifierDTO> getAllModifiers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Filter by PromptType category", required = false)
            @RequestParam(required = false) TemplatePrefix category,

            @Parameter(description = "Sort field", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        return modifierService.getModifiers(page, size, sortBy, sortDirection, category);
    }
}
