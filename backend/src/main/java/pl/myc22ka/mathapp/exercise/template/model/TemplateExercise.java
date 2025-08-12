package pl.myc22ka.mathapp.exercise.template.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "template_exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String difficulty;

    @Column(name = "template_text", columnDefinition = "TEXT")
    private String templateText;

    @Column(name = "template_answer", columnDefinition = "TEXT")
    private String templateAnswer;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<Step> steps = List.of();
}