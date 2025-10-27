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
    private final Random daily = new Random();

    @Getter
    private Exercise lastDailyExercise;

    /**
     * Initializes the scheduler by picking the first Daily exercise on startup.
     */
    @PostConstruct
    public void init() {
        pickDailyExercise();
    }

    /**
     * Picks a Daily exercise from all available exercises.
     * Runs every day at 9 AM.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void pickDailyExercise() {
        List<Exercise> allExercises = exerciseRepository.findAll();

        if (!allExercises.isEmpty()) {
            lastDailyExercise = allExercises.get(daily.nextInt(allExercises.size()));
        }
    }

}
