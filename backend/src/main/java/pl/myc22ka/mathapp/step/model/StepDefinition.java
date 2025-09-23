package pl.myc22ka.mathapp.step.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "step_definition")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_text", columnDefinition = "TEXT", nullable = false)
    private String stepText;

    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", nullable = false)
    private StepType stepType;
}
