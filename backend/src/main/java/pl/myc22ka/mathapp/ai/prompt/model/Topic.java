package pl.myc22ka.mathapp.ai.prompt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a topic for prompts.
 * <p>
 * Holds prompt type and associated text, plus linked prompts.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Entity
@Table(name = "topics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PromptType type;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prompt> prompts = new ArrayList<>();
}
