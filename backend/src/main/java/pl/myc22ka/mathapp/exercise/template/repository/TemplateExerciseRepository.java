package pl.myc22ka.mathapp.exercise.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

import java.util.Optional;

/**
 * Repository for managing {@link TemplateExercise} entities.
 * Provides CRUD operations and query method support via Spring Data JPA.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 13.09.2025
 */
@Repository
public interface TemplateExerciseRepository extends
        JpaRepository<TemplateExercise, Long>,
        JpaSpecificationExecutor<TemplateExercise> {
}
