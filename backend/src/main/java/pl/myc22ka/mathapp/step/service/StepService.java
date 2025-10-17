package pl.myc22ka.mathapp.step.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.dto.StepDTO;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Service for retrieving and managing step definitions.
 * Provides methods to get steps with pagination, sorting, and optional filtering by type or category.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @see StepDefinition
 * @see StepDTO
 * @since 17.10.2025
 */
@Service
@RequiredArgsConstructor
public class StepService {

    private final StepDefinitionRepository stepDefinitionRepository;

    /**
     * Retrieves a paginated list of steps with optional filtering by step type or category.
     *
     * @param page          zero-based page index
     * @param size          number of items per page
     * @param stepType      optional filter by step type
     * @param category      optional filter by template category
     * @param sortBy        field to sort by
     * @param sortDirection sort direction, either "asc" or "desc"
     * @return a page of {@link StepDTO} representing the step definitions
     */
    public Page<StepDTO> getSteps(int page, int size, StepType stepType,
                                  TemplatePrefix category, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<StepDefinition> stepDefinitions;

        if (stepType != null) {
            stepDefinitions = stepDefinitionRepository.findByStepType(stepType, pageable);
        } else if (category != null) {
            List<StepType> stepTypesForCategory = Arrays.stream(StepType.values())
                    .filter(type -> type.getCategory() == category)
                    .toList();
            stepDefinitions = stepDefinitionRepository.findByStepTypeIn(stepTypesForCategory, pageable);
        } else {
            stepDefinitions = stepDefinitionRepository.findAll(pageable);
        }

        return stepDefinitions.map(StepDTO::fromStepDefinition);
    }
}
