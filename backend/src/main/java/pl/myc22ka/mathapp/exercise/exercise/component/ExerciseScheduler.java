package pl.myc22ka.mathapp.exercise.exercise.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.service.ExerciseService;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ExerciseScheduler {

    private final ExerciseService exerciseService;
    private final Random random = new Random();

    @Getter
    private Exercise lastRandomExercise;

    @Scheduled(cron = "0 0 9 * * *")
    public void pickRandomExercise() {
        List<Exercise> allExercises = exerciseService.getAll();

        if (!allExercises.isEmpty()) {
            lastRandomExercise = allExercises.get(random.nextInt(allExercises.size()));
        }
    }

}
