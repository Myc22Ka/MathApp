package pl.myc22ka.mathapp.exercise.exercise.component;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseDTO;
import pl.myc22ka.mathapp.exercise.exercise.service.ExerciseService;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ExerciseScheduler {

    private final ExerciseService exerciseService;
    private final Random random = new Random();

    private ExerciseDTO lastRandomExercise;

    // Co 1 minutę (możesz zmienić cron/interval)
    @Scheduled(fixedRate = 60000)
    public void pickRandomExercise() {
        List<ExerciseDTO> allExercises = exerciseService.findAll();

        if (!allExercises.isEmpty()) {
            lastRandomExercise = allExercises.get(random.nextInt(allExercises.size()));
        }
    }

    public Optional<ExerciseDTO> getLastRandomExercise() {
        return Optional.ofNullable(lastRandomExercise);
    }
}
