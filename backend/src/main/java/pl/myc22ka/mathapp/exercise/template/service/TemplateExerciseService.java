package pl.myc22ka.mathapp.exercise.template.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.exercise.template.component.filter.TemplateExerciseSpecification;
import pl.myc22ka.mathapp.exercise.template.component.helper.TemplateExerciseHelper;
import pl.myc22ka.mathapp.exercise.template.dto.TemplateExerciseDTO;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;
import pl.myc22ka.mathapp.utils.resolver.component.TemplateResolver;

/**
 * Service layer for managing {@link TemplateExercise} entities.
 * Provides methods for creating, retrieving, updating, and deleting template exercises.
 * Delegates validation and preparation logic to {@link TemplateExerciseHelper}.
 *
 * @author Myc22Ka
 * @version 1.0.4
 * @since 13.09.2025
 */
@Service
@RequiredArgsConstructor
public class TemplateExerciseService {

    private final TemplateExerciseRepository templateRepository;
    private final TemplateExerciseHelper templateExerciseHelper;
    private final StepDefinitionRepository stepDefinitionRepository;
    private final TemplateResolver templateResolver;

    /**
     * Creates a new template exercise.
     * Validates the DTO and prepares the entity before persisting.
     *
     * @param dto the {@link TemplateExerciseDTO} containing data for creation
     */
    public void create(@NotNull TemplateExerciseDTO dto) {
        TemplateExercise template = dto.toEntity(stepDefinitionRepository);

        templateExerciseHelper.prepareForCreate(template);

        templateRepository.save(template);
    }

    /**
     * Retrieves a paginated list of template exercises with optional filtering and sorting.
     *
     * @param page          zero-based page index
     * @param size          number of items per page
     * @param difficulty    optional difficulty filter
     * @param category      optional category filter
     * @param sortBy        field name to sort by
     * @param sortDirection "asc" or "desc" sorting direction
     * @return a {@link Page} of {@link TemplateExerciseDTO} matching the criteria
     */
    public Page<TemplateExerciseDTO> getAllTemplates(int page, int size,
                                                     String difficulty, TemplatePrefix category,
                                                     String sortBy, @NotNull String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<TemplateExercise> spec = TemplateExerciseSpecification.withFilters(
                difficulty, category);

        return templateRepository.findAll(spec, pageable).map(TemplateExerciseDTO::fromEntity);
    }

    /**
     * Retrieves a template exercise by its ID.
     *
     * @param id the template exercise ID
     * @return the {@link TemplateExercise} entity
     * @throws IllegalArgumentException if template not found
     */
    public TemplateExercise getById(Long id) {
        return templateExerciseHelper.getTemplate(id);
    }

    /**
     * Updates an existing template exercise.
     * Determines whether a soft update (minor changes) or hard update (full replacement) should be applied.
     *
     * @param id  the ID of the template exercise to update
     * @param dto the {@link TemplateExerciseDTO} containing updated data
     */
    @Transactional
    public void update(Long id, @NotNull TemplateExerciseDTO dto) {
        TemplateExercise existing = templateExerciseHelper.getTemplate(id);
        TemplateExercise updated = dto.toEntity(stepDefinitionRepository);

        updated.setClearText(templateResolver.removeTemplatePlaceholders(updated.getTemplateText()));

        if (templateExerciseHelper.isSoftUpdate(existing, updated)) {
            templateExerciseHelper.applySoftUpdate(existing, updated);
        } else {
            templateExerciseHelper.applyHardUpdate(existing, updated);
        }

        templateRepository.save(existing);
    }

    /**
     * Deletes a template exercise by its ID.
     *
     * @param id the ID of the template exercise to delete
     */
    public void delete(Long id) {
        templateRepository.deleteById(id);
    }
}