package pl.myc22ka.mathapp.exercise.variant.model;

import jakarta.persistence.*;
import lombok.*;
import pl.myc22ka.mathapp.exercise.template.component.TemplateLike;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a variant of a template exercise.
 * Each variant can have its own text, answer, difficulty, steps, and track how many exercises were generated from it.
 *
 * @author Myc22Ka
 * @version 1.1.0
 * @since 13.09.2025
 */
@Entity
@Table(name = "template_exercise_variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateExerciseVariant implements TemplateLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_exercise_id", nullable = false)
    private TemplateExercise templateExercise;

    @Enumerated(EnumType.STRING)
    private TemplatePrefix category;

    private String difficulty;

    @Column(name = "template_text", columnDefinition = "TEXT")
    private String templateText;

    @Column(name = "template_answer", columnDefinition = "TEXT")
    private String templateAnswer;

    @Column(name = "clear_text", columnDefinition = "TEXT")
    private String clearText;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StepWrapper> steps = new ArrayList<>();

    @Column(name = "exercise_counter")
    private Long exerciseCounter;

    @Column(name = "points", nullable = false)
    @Builder.Default
    private Double points = 1.0;

    @Column(name = "required_level", nullable = false)
    private Integer requiredLevel;
}
