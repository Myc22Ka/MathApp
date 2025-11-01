package pl.myc22ka.mathapp.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.model.UserExercise;

import java.util.Optional;

/**
 * Repository for managing UserExercise entities.
 * <p>
 * Provides methods to find a user's progress for a specific exercise.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
public interface UserExerciseRepository extends JpaRepository<UserExercise, Long> {

    /**
     * Finds the UserExercise record for a given user and exercise.
     *
     * @param user     the user
     * @param exercise the exercise
     * @return an optional UserExercise
     */
    Optional<UserExercise> findByUserAndExercise(User user, Exercise exercise);

    /**
     * Finds the UserExercise record by user ID and exercise ID.
     *
     * @param userId     the user's ID
     * @param exerciseId the exercise's ID
     * @return an optional UserExercise
     */
    Optional<UserExercise> findByUserIdAndExerciseId(Long userId, Long exerciseId);
}
