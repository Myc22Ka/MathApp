package pl.myc22ka.mathapp.step.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single step in a math exercise.
 * Each step has a type (like union, intersection, complement) and descriptive text.
 * Used in exercises to define the operations the user should perform.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
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
