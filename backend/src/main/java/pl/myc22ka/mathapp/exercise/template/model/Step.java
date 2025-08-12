package pl.myc22ka.mathapp.exercise.template.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "order_index")
    private int orderIndex;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TemplateExercise exercise;
}
