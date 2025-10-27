package pl.myc22ka.mathapp.user.model;

import jakarta.persistence.*;
import lombok.*;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_exercises", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "exercise_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "solved", nullable = false)
    @Builder.Default
    private boolean solved = false;

    @Column(name = "solved_at")
    private LocalDateTime solvedAt;
}