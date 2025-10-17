package pl.myc22ka.mathapp.exercise.exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;

/**
 * Repository for Exercise entities.
 * Provides CRUD operations and query methods for Exercise.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 13.09.2025
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long>, JpaSpecificationExecutor<Exercise> {
}
