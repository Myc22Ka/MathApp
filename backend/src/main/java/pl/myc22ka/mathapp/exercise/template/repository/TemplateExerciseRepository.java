package pl.myc22ka.mathapp.exercise.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

/**
 * Repository for managing {@link TemplateExercise} entities.
 * Provides CRUD operations and query method support via Spring Data JPA.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Repository
public interface TemplateExerciseRepository extends JpaRepository<TemplateExercise, Long> {
}
