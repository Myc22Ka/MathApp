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
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;

/**
 * Service layer for managing {@link TemplateExercise} entities.
 * Provides methods for creating, retrieving, updating, and deleting template exercises.
 * Delegates validation and preparation logic to {@link TemplateExerciseHelper}.
 *
 * @author Myc22Ka
 * @version 1.0.3
 * @since 13.09.2025
 */
@Service
@RequiredArgsConstructor
public class TemplateExerciseService {

    private final TemplateExerciseRepository templateRepository;
    private final TemplateExerciseHelper templateExerciseHelper;
    private final StepDefinitionRepository stepDefinitionRepository;

    /**
     * Creates a new template exercise after validating uniqueness and preparing required fields.
     *
     * @param template the template exercise to create
     */
    public void create(@NotNull TemplateExerciseDTO dto) {
        TemplateExercise template = dto.toEntity(stepDefinitionRepository);

        templateExerciseHelper.validateUnique(template);
        templateExerciseHelper.prepareForCreate(template);

        templateRepository.save(template);
    }

    /**
     * Retrieves all template exercises.
     *
     * @return list of all template exercises
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
     * Retrieves a template exercise by its id.
     *
     * @param id the template exercise id
     * @return the template exercise
     * @throws IllegalArgumentException if not found
     */
    public TemplateExercise getById(Long id) {
        return templateExerciseHelper.getTemplate(id);
    }

    /**
     * Updates an existing template exercise.
     * Decides between soft update (minor changes) and hard update (full replacement).
     *
     * @param id      the id of the template exercise to update
     * @param updated the updated template exercise data
     *                TODO: Consider better comments here :)
     */
    @Transactional
    public void update(Long id, @NotNull TemplateExerciseDTO dto) {
        TemplateExercise existing = templateExerciseHelper.getTemplate(id);
        TemplateExercise updated = dto.toEntity(stepDefinitionRepository);

        if (templateExerciseHelper.isSoftUpdate(existing, updated)) {
            templateExerciseHelper.applySoftUpdate(existing, updated);
        } else {
            templateExerciseHelper.applyHardUpdate(existing, updated);
        }

        templateRepository.save(existing);
    }

    /**
     * Deletes a template exercise by its id.
     *
     * @param id the id of the template exercise to delete
     */
    public void delete(Long id) {
        templateRepository.deleteById(id);
    }
}