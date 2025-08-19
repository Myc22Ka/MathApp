package pl.myc22ka.mathapp.exercise.exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;

public interface ExerciseRepository  extends JpaRepository<Exercise, Long>  {
}
