package pl.myc22ka.mathapp.user.component.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.model.UserExercise;
import pl.myc22ka.mathapp.user.repository.UserExerciseRepository;

import java.time.LocalDateTime;

/**
 * Helper class for managing the relationship between Users and Exercises.
 * Provides methods to retrieve, create, and update UserExercise records.
 * Ensures that exercises can be marked as solved and queried efficiently.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Component
@RequiredArgsConstructor
public class UserExerciseHelper {

    private final UserExerciseRepository userExerciseRepository;

    /**
     * Retrieves the UserExercise record for the given user and exercise.
     * If it does not exist, creates a new record with `solved` set to false.
     *
     * @param user     the user
     * @param exercise the exercise
     * @return the existing or newly created UserExercise record
     */
    public UserExercise getOrCreate(User user, Exercise exercise) {
        return userExerciseRepository.findByUserAndExercise(user, exercise)
                .orElse(UserExercise.builder()
                        .user(user)
                        .exercise(exercise)
                        .solved(false)
                        .build());
    }

    /**
     * Marks the given exercise as solved by the user.
     * If the exercise was already solved, no action is performed.
     *
     * @param user     the user
     * @param exercise the exercise to mark as solved
     */
    public void markAsSolved(User user, Exercise exercise) {
        UserExercise userExercise = getOrCreate(user, exercise);
        if (!userExercise.isSolved()) {
            userExercise.setSolved(true);
            userExercise.setSolvedAt(LocalDateTime.now());

            userExerciseRepository.save(userExercise);
        }
    }

    /**
     * Checks if the given exercise has been solved by the user.
     *
     * @param user     the user
     * @param exercise the exercise
     * @return true if the exercise is solved, false otherwise
     */
    public boolean isSolved(User user, Exercise exercise) {
        return userExerciseRepository.findByUserAndExercise(user, exercise)
                .map(UserExercise::isSolved)
                .orElse(false);
    }

    /**
     * Checks if the exercise has been solved by a user given the user ID.
     * Returns false if the userId is null or no record exists.
     *
     * @param userId   the ID of the user
     * @param exercise the exercise
     * @return true if solved, false otherwise
     */
    public boolean isSolved(Long userId, Exercise exercise) {
        if (userId == null) return false;

        return userExerciseRepository.findByUserIdAndExerciseId(userId, exercise.getId())
                .map(UserExercise::isSolved)
                .orElse(false);
    }
}
