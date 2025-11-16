package pl.myc22ka.mathapp.exercise.daily;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.exercise.daily.component.CronParser;
import pl.myc22ka.mathapp.exercise.daily.component.ExerciseScheduler;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.service.ExerciseService;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.user.model.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DailyExerciseService {

    private final ExerciseScheduler exerciseScheduler;
    private final ExerciseService exerciseService;
    private final UserHelper userHelper;

    @Value("${spring.scheduler.daily-exercise-cron}")
    private String cronExpression;

    @Transactional
    public boolean solveDaily(User user, String answer) {
        Exercise dailyExercise = exerciseScheduler.getLastDailyExercise();
        if (dailyExercise == null) {
            throw new IllegalStateException("Daily exercise is not set yet.");
        }

        boolean solved = exerciseService.solve(
                user,
                dailyExercise.getId(),
                answer
        );

        if (solved) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime previousExecution = CronParser.getPreviousExecution(cronExpression);
            long intervalMinutes = CronParser.getMinutesBetweenExecutions(cronExpression);

            if (user.getLastDailyTaskDate() == null || user.getLastDailyTaskDate().isBefore(previousExecution)) {
                user.updateStreak(now, intervalMinutes);
            }

            user.setLastDailyTaskDate(now);
            userHelper.save(user);
        }

        return solved;
    }
}
