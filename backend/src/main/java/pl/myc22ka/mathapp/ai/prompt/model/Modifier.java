package pl.myc22ka.mathapp.ai.prompt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base entity for all modifiers.
 * <p>
 * Uses single-table inheritance with a discriminator column
 * to distinguish modifier types (e.g., Difficulty, Requirement, Template).
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Entity
@Table(name = "modifiers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "modifier_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Modifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modifier_text", nullable = false, columnDefinition = "TEXT")
    private String modifierText;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}
