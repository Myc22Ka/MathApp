package pl.myc22ka.mathapp.ai.prompt.model;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;

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

    @Column(name = "modifier_text", nullable = false)
    private String modifierText;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    public abstract Modifier findOrCreate(@NotNull ModifierRepository repository);
}
