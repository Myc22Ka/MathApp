package pl.myc22ka.mathapp.ai.prompt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;

/**
 * Service for handling Modifier-related operations.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Service
@RequiredArgsConstructor
public class ModifierService {

    private final ModifierRepository modifierRepository;

    /**
     * Returns the maximum difficulty level for a given topic.
     * Defaults to 1 if none found.
     *
     * @param topic the topic
     * @return max difficulty level
     */
    public int getMaxDifficultyLevel(Topic topic) {
        return modifierRepository.findMaxDifficultyLevelByTopic(topic).orElse(1);
    }
}
