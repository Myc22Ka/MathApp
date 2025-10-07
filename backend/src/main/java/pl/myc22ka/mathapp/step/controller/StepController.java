package pl.myc22ka.mathapp.step.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.dto.StepDTO;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.service.StepService;

/**
 * REST controller for managing steps.
 * <p>
 * Provides an endpoint for retrieving steps with filtering, sorting and pagination.
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 24.09.2025
 */
@RestController
@RequestMapping("api/steps")
@RequiredArgsConstructor
@Tag(
        name = "Steps",
        description = "API endpoints for retrieving steps"
)
public class StepController {

    private final StepService stepService;

    /**
     * Retrieves a paginated list of steps with optional filtering and sorting.
     *
     * @param page          Zero-based page index (default: 0)
     * @param size          Number of items per page (default: 20)
     * @param stepType      Optional filter by step type
     * @param category      Optional filter by category (prompt type)
     * @param sortBy        Field name used for sorting (default: id)
     * @param sortDirection Sorting direction: "asc" or "desc" (default: asc)
     * @return Paginated list of steps matching the criteria
     */
    @Operation(
            summary = "Get steps",
            description = "Returns a paginated list of steps with optional filters, sorting and pagination parameters."
    )
    @GetMapping
    public Page<StepDTO> getSteps(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) StepType stepType,
            @RequestParam(required = false) TemplatePrefix category,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        return stepService.getSteps(page, size, stepType, category, sortBy, sortDirection);
    }
}

