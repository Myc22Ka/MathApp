package pl.myc22ka.mathapp.exercise.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExerciseVariant;

public interface TemplateExerciseVariantRepository extends JpaRepository<TemplateExerciseVariant, Long> {
}
