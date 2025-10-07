package pl.myc22ka.mathapp.ai.prompt.component.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.repository.TopicRepository;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

@Component
@RequiredArgsConstructor
public class TopicHelper {

    private final TopicRepository topicRepository;

    public Topic findTopicByType(TemplatePrefix topicType) {
        return topicRepository.findFirstByType(topicType)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Topic not found for prefix: " + topicType));
    }

}
