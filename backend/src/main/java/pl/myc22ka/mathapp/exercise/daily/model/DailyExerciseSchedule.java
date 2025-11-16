package pl.myc22ka.mathapp.exercise.daily.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_exercise_schedules", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"schedule_date"})
})
@Data
@NoArgsConstructor
public class DailyExerciseSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "schedule_date", nullable = false)
    private LocalDateTime scheduleDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
