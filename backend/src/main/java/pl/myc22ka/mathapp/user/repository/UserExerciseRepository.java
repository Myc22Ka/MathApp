package pl.myc22ka.mathapp.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.model.UserExercise;

import java.util.Optional;

public interface UserExerciseRepository extends JpaRepository<UserExercise, Long> {
    Optional<UserExercise> findByUserAndExercise(User user, Exercise exercise);

    Optional<UserExercise> findByUserIdAndExerciseId(Long userId, Long exerciseId);
}
