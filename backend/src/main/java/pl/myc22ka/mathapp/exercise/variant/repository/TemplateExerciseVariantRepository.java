package pl.myc22ka.mathapp.exercise.variant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.List;

public interface TemplateExerciseVariantRepository extends JpaRepository<TemplateExerciseVariant, Long> {
    List<TemplateExerciseVariant> findByTemplateExerciseId(Long exerciseId);
}
