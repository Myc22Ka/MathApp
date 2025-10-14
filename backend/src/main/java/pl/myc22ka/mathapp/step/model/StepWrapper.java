package pl.myc22ka.mathapp.step.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a single step in solving a template exercise or its variant.
 * Each step contains text, an order index, and is linked to either a template exercise
 * or a variant.
 *
 * @author Myc22Ka
 * @version 1.1.0
 * @since 13.09.2025
 */
@Entity
@Table(name = "exercise_step")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "step_definition_id")
    private StepDefinition stepDefinition;

    @Transient
    private Long stepDefinitionId;

    @Column(name = "order_index")
    private int orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private TemplateExercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private TemplateExerciseVariant variant;

    @ElementCollection
    @CollectionTable(
            name = "step_wrapper_prefixes",
            joinColumns = @JoinColumn(name = "step_wrapper_id")
    )
    @Column(name = "prefix")
    @Builder.Default
    private List<String> prefixes = new ArrayList<>();
}

