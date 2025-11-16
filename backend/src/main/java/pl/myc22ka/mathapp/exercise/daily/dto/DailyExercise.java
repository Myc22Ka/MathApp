package pl.myc22ka.mathapp.exercise.daily.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.daily.component.CronParser;
import pl.myc22ka.mathapp.user.model.User;

import java.time.LocalDateTime;

public record DailyExercise(
        @Schema(description = "Number of daily tasks completed by the user", example = "5")
        Integer dailyTasksCompleted,

        @Schema(description = "Flag indicating if the user has completed today's daily task", example = "true")
        boolean isSolved,

        @Schema(description = "Date and time of the last completed daily task", example = "2025-11-08T14:37:21")
        LocalDateTime lastDailyTaskDate,

        @Schema(description = "Streak of completed tasks in a row")
        Integer streak
) {
    @NotNull
    public static DailyExercise fromUser(@NotNull User user, String cronExpression) {
        LocalDateTime lastTaskDateTime = user.getLastDailyTaskDate();
        LocalDateTime previousExecution = CronParser.getPreviousExecution(cronExpression);

        boolean isSolved = lastTaskDateTime != null && lastTaskDateTime.isAfter(previousExecution);

        return new DailyExercise(
                user.getDailyTasksCompleted(),
                isSolved,
                lastTaskDateTime,
                user.getStreak()
        );
    }
}
