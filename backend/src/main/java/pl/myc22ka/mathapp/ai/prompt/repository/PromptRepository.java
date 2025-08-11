package pl.myc22ka.mathapp.ai.prompt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;

/**
 * Repository for managing Prompt entities.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {
}
