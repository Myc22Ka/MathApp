package pl.myc22ka.mathapp.user.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.model.UserExercise;
import pl.myc22ka.mathapp.user.repository.UserExerciseRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserExerciseHelper {

    private final UserExerciseRepository userExerciseRepository;

    /**
     * Pobiera rekord UserExercise dla użytkownika i ćwiczenia,
     * jeśli nie istnieje, tworzy nowy z solved=false.
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
     * Oznacza ćwiczenie jako rozwiązane przez użytkownika.
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
     * Sprawdza, czy użytkownik rozwiązał dane ćwiczenie.
     */
    public boolean isSolved(User user, Exercise exercise) {
        return userExerciseRepository.findByUserAndExercise(user, exercise)
                .map(UserExercise::isSolved)
                .orElse(false);
    }

    public boolean isSolved(Long userId, @NotNull Exercise exercise) {
        if (userId == null) return false;

        return userExerciseRepository.findByUserIdAndExerciseId(userId, exercise.getId())
                .map(UserExercise::isSolved)
                .orElse(false);
    }
}
