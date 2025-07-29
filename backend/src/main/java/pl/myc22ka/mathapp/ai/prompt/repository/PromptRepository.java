package pl.myc22ka.mathapp.ai.prompt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {
}
