package pl.myc22ka.mathapp.ai.prompt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;

@Service
@RequiredArgsConstructor
public class ModifierService {

    private final ModifierRepository modifierRepository;

    public int getMaxDifficultyLevel(Topic topic) {
        return modifierRepository.findMaxDifficultyLevelByTopic(topic).orElse(1);
    }
}
