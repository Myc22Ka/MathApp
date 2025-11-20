package pl.myc22ka.mathapp.exercise.daily.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exceptions.DefaultResponse;
import pl.myc22ka.mathapp.exercise.daily.DailyExerciseService;
import pl.myc22ka.mathapp.exercise.daily.component.ExerciseScheduler;
import pl.myc22ka.mathapp.exercise.daily.dto.DailySolveDTO;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseDTO;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.user.component.helper.UserExerciseHelper;
import pl.myc22ka.mathapp.user.model.User;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class DailyExerciseController {

    private final ExerciseScheduler exerciseScheduler;
    private final UserExerciseHelper userExerciseHelper;
    private final DailyExerciseService dailyExerciseService;

    /**
     * Retrieves a random exercise from the database.
     */
    @Operation(
            summary = "Get random exercise",
            description = "Returns a random exercise from the database."
    )
    @GetMapping("/daily")
    public ResponseEntity<ExerciseDTO> getDaily(@AuthenticationPrincipal User user) {
        Exercise exercise = exerciseScheduler.getLastDailyExercise();
        Boolean isSolved = userExerciseHelper.isSolved(user, exercise);

        return ResponseEntity.ok(ExerciseDTO.fromEntity(exercise, isSolved));
    }

    @PostMapping("/solve-daily")
    public ResponseEntity<DefaultResponse> solveDaily(@AuthenticationPrincipal User user, @NotNull @RequestBody DailySolveDTO dailySolveDTO) {
        boolean correct = dailyExerciseService.solveDaily(user, dailySolveDTO.answer());

        return ResponseEntity.ok(
                new DefaultResponse(
                        java.time.Instant.now().toString(),
                        correct ? "Answer is correct" : "Answer is incorrect",
                        correct ? 200 : 400
                )
        );
    }
}
