package pl.myc22ka.mathapp.exercise.template.model;

import jakarta.persistence.*;
import lombok.*;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.template.component.TemplateLike;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a template exercise.
 * A template defines the structure of an exercise, including placeholders,
 * category, difficulty, solution steps, and possible variants.
 *
 * @author Myc22Ka
 * @version 1.1.2
 * @since 13.09.2025
 */
@Entity
@Table(name = "template_exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateExercise implements TemplateLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TemplatePrefix category;

    private String difficulty;

    @Column(name = "template_text", columnDefinition = "TEXT")
    private String templateText;

    @Column(name = "template_answer", columnDefinition = "TEXT")
    private String templateAnswer;

    @Column(name = "clear_text", columnDefinition = "TEXT")
    private String clearText;

    @ElementCollection
    @CollectionTable(
            name = "template_exercise_prefixes",
            joinColumns = @JoinColumn(name = "template_exercise_id")
    )
    @Column(name = "prefix")
    @Builder.Default
    private List<String> templatePrefixes = new ArrayList<>();

    @Column(name = "exercise_counter")
    private Long exerciseCounter;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<StepWrapper> steps = new ArrayList<>();

    @OneToMany(mappedBy = "templateExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "templateExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TemplateExerciseVariant> variants = new ArrayList<>();
}