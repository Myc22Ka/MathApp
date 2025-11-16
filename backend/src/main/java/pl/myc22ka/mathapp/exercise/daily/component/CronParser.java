package pl.myc22ka.mathapp.exercise.daily.component;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Utility klasa do parsowania wyrażeń cron'a
 */
@UtilityClass
public class CronParser {

    public static long getMinutesBetweenExecutions(@NotNull String cronExpression) {
        String[] parts = cronExpression.split(" ");

        if (parts.length < 2) {
            return 1;
        }

        String minutePart = parts[1];

        if (minutePart.startsWith("*/")) {
            return Long.parseLong(minutePart.substring(2));
        }

        // Jeśli minuty są konkretne (np. "0"), sprawdź godziny
        if (parts.length >= 3) {
            String hourPart = parts[2];
            if (hourPart.startsWith("*/")) {
                return Long.parseLong(hourPart.substring(2)) * 60;
            }
        }

        return 1;
    }

    public static LocalDateTime getPreviousExecution(@NotNull String cronExpression) {
        String[] parts = cronExpression.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Niepoprawne wyrażenie cron: " + cronExpression);
        }

        LocalDateTime now = LocalDateTime.now();

        // minutowy
        if (parts[1].startsWith("*/")) {
            long minutes = Long.parseLong(parts[1].substring(2));
            return now.minusMinutes(minutes);
        }

        // godzinowy
        if (parts.length >= 3 && parts[2].startsWith("*/")) {
            long hours = Long.parseLong(parts[2].substring(2));
            return now.minusHours(hours);
        }

        // domyślnie raz dziennie o konkretnej godzinie
        if (parts[1].equals("0") && parts[2].matches("\\d+")) {
            int hour = Integer.parseInt(parts[2]);
            LocalDateTime scheduledToday = LocalDate.now().atTime(hour, 0);
            if (now.isAfter(scheduledToday)) {
                return scheduledToday;
            } else {
                return scheduledToday.minusDays(1);
            }
        }

        throw new UnsupportedOperationException("Ten bieda-parser nie wspiera takich cronów: " + cronExpression);
    }
}
