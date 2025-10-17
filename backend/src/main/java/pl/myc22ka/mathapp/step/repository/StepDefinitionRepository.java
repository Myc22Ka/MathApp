package pl.myc22ka.mathapp.step.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.model.StepType;

import java.util.List;

/**
 * Repository for managing {@link StepDefinition} entities.
 * Provides basic CRUD operations and methods to find steps by their type.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 * @see StepDefinition
 */
@Repository
public interface StepDefinitionRepository extends JpaRepository<StepDefinition, Long> {

    /**
     * Finds step definitions of a specific type with pagination.
     *
     * @param stepType the type of step to filter by
     * @param pageable pagination information
     * @return a page of step definitions matching the given type
     */
    Page<StepDefinition> findByStepType(StepType stepType, Pageable pageable);

    /**
     * Finds step definitions matching any of the provided step types with pagination.
     *
     * @param stepTypes the list of step types to filter by
     * @param pageable pagination information
     * @return a page of step definitions that match any of the given types
     */
    Page<StepDefinition> findByStepTypeIn(List<StepType> stepTypes, Pageable pageable);
}
