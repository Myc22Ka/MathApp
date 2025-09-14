package pl.myc22ka.mathapp.ai.prompt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;

import java.util.Optional;

/**
 * The interface Topic repository.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 23.08.2025
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    /**
     * Finds the first topic by prompt type.
     *
     * @param type the prompt type
     * @return an Optional containing the first matching Topic or empty if none found
     */
    Optional<Topic> findFirstByType(PromptType type);
}
