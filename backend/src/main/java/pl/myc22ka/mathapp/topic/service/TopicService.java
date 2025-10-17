package pl.myc22ka.mathapp.topic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.topic.dto.TopicDTO;
import pl.myc22ka.mathapp.topic.repository.TopicRepository;

import java.util.List;

/**
 * Service for managing topics.
 * <p>
 * Provides methods to retrieve topic data for API or internal use.
 * Typically used in AI prompt generation or exercise templates.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    /**
     * Retrieves all topics from the database and converts them to DTOs.
     *
     * @return list of TopicDTO representing all topics
     */
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAll()
                .stream()
                .map(TopicDTO::fromEntity)
                .toList();
    }
}
