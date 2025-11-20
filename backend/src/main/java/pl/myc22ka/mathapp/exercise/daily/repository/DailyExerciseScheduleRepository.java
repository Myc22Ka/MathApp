package pl.myc22ka.mathapp.exercise.daily.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.exercise.daily.model.DailyExerciseSchedule;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DailyExerciseScheduleRepository extends JpaRepository<DailyExerciseSchedule, Long> {
    Optional<DailyExerciseSchedule> findTopByScheduleDateBetweenOrderByScheduleDateDesc(LocalDateTime start, LocalDateTime end);
}
