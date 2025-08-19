package pl.myc22ka.mathapp.exercise.exercise.model;

import jakarta.persistence.*;
import lombok.*;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

import java.util.ArrayList;
import java.util.List;

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
}
