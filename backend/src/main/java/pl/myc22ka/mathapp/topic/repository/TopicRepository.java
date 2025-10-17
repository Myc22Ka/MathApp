package pl.myc22ka.mathapp.topic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.topic.model.Topic;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.Optional;

/**
 * The interface Topic repository.
 *
 * @author Myc22Ka
 * @version 1.0.2
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
    Optional<Topic> findFirstByType(TemplatePrefix type);
}
