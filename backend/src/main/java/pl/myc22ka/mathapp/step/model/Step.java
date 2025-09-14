package pl.myc22ka.mathapp.step.model;

import jakarta.persistence.*;
import lombok.*;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

/**
 * Entity representing a single step in solving a template exercise or its variant.
 * Each step contains text, an order index, and is linked to either a template exercise
 * or a variant.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
@Entity
@Table(name = "exercise_step")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_text", columnDefinition = "TEXT")
    private String stepText;

    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", nullable = false)
    private StepType stepType;

    @Column(name = "order_index")
    private int orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private TemplateExercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private TemplateExerciseVariant variant;
}
