package pl.myc22ka.mathapp.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;

import java.time.LocalDateTime;

/**
 * Represents the relation between a user and an exercise.
 * <p>
 * Tracks whether the user has solved the exercise and when.
 * Ensures that each user-exercise pair is unique.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
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