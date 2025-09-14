package pl.myc22ka.mathapp.exercise.exercise.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Exercise entity stored in the database.
 * Links to a TemplateExercise and stores text, values, and verification status.
 *
 * @author Myc22Ka
 * @version 1.0.0
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

    @ElementCollection
    @CollectionTable(
            name = "exercise_values",
            joinColumns = @JoinColumn(name = "exercise_id")
    )
    @Column(name = "value", columnDefinition = "TEXT")
    @OrderColumn(name = "value_order")
    @Builder.Default
    private List<String> values = new ArrayList<>();

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "verified", nullable = false)
    @Builder.Default
    private boolean verified = false;
}
