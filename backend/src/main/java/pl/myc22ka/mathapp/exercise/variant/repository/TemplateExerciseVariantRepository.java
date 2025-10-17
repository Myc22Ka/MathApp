package pl.myc22ka.mathapp.exercise.variant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing {@link TemplateExerciseVariant} entities.
 * Provides CRUD operations and custom queries for variants of template exercises.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 13.09.2025
 */
@Repository
public interface TemplateExerciseVariantRepository extends JpaRepository<TemplateExerciseVariant, Long>, JpaSpecificationExecutor<TemplateExerciseVariant> {

    /**
     * Finds all variants associated with a given template exercise.
     *
     * @param exerciseId the ID of the parent template exercise
     * @return list of template exercise variants
     */
    List<TemplateExerciseVariant> findByTemplateExerciseId(Long exerciseId);

    /**
     * Finds a {@link TemplateExerciseVariant} by its ID and eagerly fetches its parent
     * {@link pl.myc22ka.mathapp.exercise.template.model.TemplateExercise}.
     *
     * @param id the ID of the variant to retrieve
     * @return an {@link Optional} containing the found variant, if present
     */
    @Query("""
        SELECT v
        FROM TemplateExerciseVariant v
        JOIN FETCH v.templateExercise
        WHERE v.id = :id
    """)
    Optional<TemplateExerciseVariant> findByIdWithTemplate(@Param("id") Long id);
}
