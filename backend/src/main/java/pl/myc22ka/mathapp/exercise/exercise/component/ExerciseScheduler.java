package pl.myc22ka.mathapp.exercise.exercise.component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;

import java.util.List;
import java.util.Random;

/**
 * Schedules and picks a random exercise daily at 9 AM.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Component
@RequiredArgsConstructor
public class ExerciseScheduler {

    private final ExerciseRepository exerciseRepository;
    private final Random random = new Random();

    @Getter
    private Exercise lastRandomExercise;

    /**
     * Initializes the scheduler by picking the first random exercise on startup.
     */
    @PostConstruct
    public void init() {
        pickRandomExercise();
    }

    /**
     * Picks a random exercise from all available exercises.
     * Runs every day at 9 AM.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void pickRandomExercise() {
        List<Exercise> allExercises = exerciseRepository.findAll();

        if (!allExercises.isEmpty()) {
            lastRandomExercise = allExercises.get(random.nextInt(allExercises.size()));
        }
    }

}
