package pl.myc22ka.mathapp.ai.ollama.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.myc22ka.mathapp.ai.prompt.component.helper.PromptHelper;
import pl.myc22ka.mathapp.topic.component.helper.TopicHelper;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionRequest;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.topic.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.service.PromptService;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;

import java.util.List;
import java.util.Map;

/**
 * Service for handling communication with the Ollama AI model.
 * <p>
 * This service builds prompts, sends them to the Ollama API, parses the responses
 * into mathematical expressions, and validates user-provided expressions.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 06.08.2025
 * @see PromptService
 * @see ExpressionFactory
 * @see TopicHelper
 * @see PromptHelper
 */
@Service
@RequiredArgsConstructor
public class OllamaService {

    @Value("${spring.ollama.base-url}")
    private String baseUrl;

    @Value("${spring.ollama.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final PromptService promptService;
    private final ExpressionFactory expressionFactory;
    private final TopicHelper topicHelper;
    private final PromptHelper promptHelper;

    /**
     * Sends prompt to Ollama AI and returns response.
     *
     * @param prompt text prompt for AI
     * @return AI response as string
     * @throws RuntimeException if request fails
     */
    public String chat(String prompt) {
        String url = baseUrl + "/api/generate";

        Map<String, Object> body = Map.of(
                "model", model,
                "prompt", prompt,
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        var response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("response");
        } else {
            throw new RuntimeException("Failed to get response from Ollama");
        }
    }

    /**
     * Generates a new math prompt, sends it to AI, parses and verifies the response,
     * and saves the result.
     *
     * @param request request object containing topic type and prompt details
     * @return the completed {@link Prompt} with AI response and parsed expression
     */
    public Prompt generatePrompt(MathExpressionChatRequest request) {
        Prompt prompt = promptService.createPrompt(request);

        String response = chat(prompt.getFinalPromptText());

        var parsed = expressionFactory.parse(new ContextRecord(request.topicType(), response));

        prompt.setResponseText(parsed.toString());

        promptHelper.verifyPromptResponse(prompt, parsed);

        promptHelper.save(prompt);

        return prompt;
    }

    /**
     * Validates user's math expression request.
     *
     * @param request math expression to verify
     * @return true if expression is valid
     */
    public boolean useMathString(@NotNull MathExpressionRequest request) {
        Topic topic = topicHelper.findTopicByType(request.topicType());
        List<Modifier> modifiers = promptHelper.findModifiers(request.modifiers(), topic);

        var parsed = expressionFactory.parse(new ContextRecord(request.topicType(), request.response()));

        return promptHelper.verify(parsed, request.topicType(), modifiers);
    }
}