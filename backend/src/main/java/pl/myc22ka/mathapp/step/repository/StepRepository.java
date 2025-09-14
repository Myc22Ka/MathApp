package pl.myc22ka.mathapp.step.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.step.model.Step;

import java.util.List;

public interface StepRepository extends JpaRepository<Step, Long> {
    List<Step> findByExerciseIdOrderByOrderIndex(Long exerciseId);
}
