package pl.myc22ka.mathapp.exercise.daily.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exercise.daily.model.DailyExerciseSchedule;
import pl.myc22ka.mathapp.exercise.daily.repository.DailyExerciseScheduleRepository;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;
import pl.myc22ka.mathapp.exercise.exercise.service.ExerciseService;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.repository.TemplateExerciseVariantRepository;
import pl.myc22ka.mathapp.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Schedules and picks a random exercise daily at 9 AM.
 * Resets all users' daily task completion status when new daily exercise is created.
 *
 * @author Myc22Ka
 * @version 1.1.0
 * @since 13.09.2025
 */
@Component
@RequiredArgsConstructor
public class ExerciseScheduler {
    private final ExerciseRepository exerciseRepository;
    private final DailyExerciseScheduleRepository dailyScheduleRepository;
    private final UserRepository userRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final TemplateExerciseVariantRepository templateExerciseVariantRepository;
    private final ExerciseService exerciseService;
    private final Random random = new Random();

    @Value("${spring.scheduler.daily-exercise-cron}")
    private String cronExpression;

    @Scheduled(cron = "${spring.scheduler.daily-exercise-cron}")
    public void pickDailyExercise() {
        LocalDateTime executionTime = LocalDateTime.now();
        long intervalMinutes = CronParser.getMinutesBetweenExecutions(cronExpression);

        Exercise generated = generateRandomExercise();
        DailyExerciseSchedule schedule = new DailyExerciseSchedule();
        schedule.setExercise(generated);
        schedule.setScheduleDate(executionTime);
        schedule.setCreatedAt(executionTime);
        DailyExerciseSchedule saved = dailyScheduleRepository.save(schedule);
        if (saved.getId() == null) {
            throw new RuntimeException("Failed to save daily exercise schedule");
        }

        userRepository.resetBrokenStreaks(executionTime, intervalMinutes);
        userRepository.resetDailyTaskDateForAll(intervalMinutes);
    }

    /**
     * Generuje nowe ćwiczenie z losowo wybranego template'u lub wariantu
     */
    private Exercise generateRandomExercise() {
        boolean useTemplate = random.nextBoolean();
        if (useTemplate) {
            TemplateExercise template = getRandomTemplate();
            return exerciseService.generate(template.getId(), null);
        } else {
            TemplateExerciseVariant variant = getRandomVariant();
            return exerciseService.generate(null, variant.getId());
        }
    }

    private TemplateExercise getRandomTemplate() {
        List<TemplateExercise> allTemplates = templateExerciseRepository.findAll();
        return allTemplates.get(random.nextInt(allTemplates.size()));
    }

    private TemplateExerciseVariant getRandomVariant() {
        List<TemplateExerciseVariant> allVariants = templateExerciseVariantRepository.findAll();
        return allVariants.get(random.nextInt(allVariants.size()));
    }

    public Exercise getLastDailyExercise() {
        LocalDate today = LocalDate.now();
        return dailyScheduleRepository.findTopByScheduleDateBetweenOrderByScheduleDateDesc(
                        today.atStartOfDay(),
                        today.atTime(23, 59, 59))
                .map(DailyExerciseSchedule::getExercise)
                .orElseThrow(() -> new RuntimeException("No daily exercise scheduled for today"));
    }
}