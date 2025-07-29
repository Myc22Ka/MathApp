package pl.myc22ka.mathapp.ai.prompt.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prompts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToMany
    @JoinTable(name = "prompt_modifiers")
    private List<Modifier> modifiers = new ArrayList<>();

    @Column(name = "final_prompt_text", nullable = false, columnDefinition = "TEXT")
    private String finalPromptText;

    @Column(name = "response_text", columnDefinition = "TEXT")
    private String responseText;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void buildFinalPromptText() {
        StringBuilder sb = new StringBuilder();

        if (topic != null && topic.getText() != null) {
            sb.append(topic.getText());
        }

        if (modifiers != null && !modifiers.isEmpty()) {
            modifiers.forEach(mod -> {
                if (mod.getModifierText() != null) {
                    sb.append(" ").append(mod.getModifierText());
                }
            });
        }

        this.finalPromptText = sb.toString().trim();
    }
}
