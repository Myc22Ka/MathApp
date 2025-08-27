package pl.myc22ka.mathapp.exercise.template.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "template_exercise_variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateExerciseVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_exercise_id", nullable = false)
    private TemplateExercise templateExercise;

    private String difficulty;

    @Column(name = "variant_text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "variant_answer", columnDefinition = "TEXT")
    private String answer;
}
