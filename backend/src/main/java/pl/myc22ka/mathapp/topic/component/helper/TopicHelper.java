package pl.myc22ka.mathapp.topic.component.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.topic.model.Topic;
import pl.myc22ka.mathapp.topic.repository.TopicRepository;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Helper class for managing and retrieving topics.
 * <p>
 * Provides utility methods for finding topics based on their template prefix.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Component
@RequiredArgsConstructor
public class TopicHelper {

    private final TopicRepository topicRepository;

    /**
     * Finds the first topic matching the given template prefix.
     *
     * @param topicType type of the topic to search for
     * @return the found {@link Topic}
     * @throws IllegalArgumentException if no topic with the given prefix exists
     */
    public Topic findTopicByType(TemplatePrefix topicType) {
        return topicRepository.findFirstByType(topicType)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Topic not found for prefix: " + topicType));
    }

}
