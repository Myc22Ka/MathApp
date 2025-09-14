package pl.myc22ka.mathapp.exercise.variant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.List;

/**
 * Repository for managing {@link TemplateExerciseVariant} entities.
 * Provides CRUD operations and custom queries for variants of template exercises.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Repository
public interface TemplateExerciseVariantRepository extends JpaRepository<TemplateExerciseVariant, Long> {

    /**
     * Finds all variants associated with a given template exercise.
     *
     * @param exerciseId the ID of the parent template exercise
     * @return list of template exercise variants
     */
    List<TemplateExerciseVariant> findByTemplateExerciseId(Long exerciseId);
}
