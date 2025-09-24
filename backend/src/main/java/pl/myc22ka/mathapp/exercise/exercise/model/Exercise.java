package pl.myc22ka.mathapp.exercise.exercise.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.myc22ka.mathapp.exercise.exercise.annotation.rating.Rating;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

/**
 * Represents an Exercise entity stored in the database.
 * Links to a TemplateExercise and stores text, values, and verification status.
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 13.09.2025
 */
@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_exercise_id", nullable = false)
    private TemplateExercise templateExercise;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @Column(name = "verified", nullable = false)
    @Builder.Default
    private boolean verified = false;

    @Column(name = "rating")
    @Rating
    private Double rating;

    @Column(name = "context", columnDefinition = "TEXT")
    private String contextJson;
}

