package pl.myc22ka.mathapp.exercise.exercise.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.myc22ka.mathapp.exercise.exercise.annotation.rating.Rating;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

/**
 * Represents an Exercise entity stored in the database.
 * Links to a TemplateExercise and stores text, values, and verification status.
 *
 * @author Myc22Ka
 * @version 1.0.3
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
    @JoinColumn(name = "template_exercise_id")
    private TemplateExercise templateExercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_exercise_variant_id")
    private TemplateExerciseVariant templateExerciseVariant;

    @Column(name = "text", columnDefinition = "TEXT", nullable = false, unique = true)
    private String text;

    @Column(name = "answer", columnDefinition = "TEXT", nullable = false)
    private String answer;

    @Column(name = "verified", nullable = false)
    @Builder.Default
    private boolean verified = false;

    @Column(name = "rating", nullable = false)
    @Rating
    private Double rating = 0.0;

    @Column(name = "context", columnDefinition = "TEXT")
    private String contextJson;
}

