package pl.myc22ka.mathapp.ai.ollama.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionRequest;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.ai.prompt.service.PromptService;

import java.util.Map;

/**
 * Service for Ollama AI communication and math expression generation.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 06.08.2025
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
     * Generates and verifies math expression using AI.
     *
     * @param request request with expression parameters
     * @return generated math expression
     */
    public String generateMathExpression(MathExpressionChatRequest request) {
        Prompt prompt = promptService.createPrompt(request);

        String response = chat(prompt.getFinalPromptText()).replace("\n", "").replaceFirst("^\\+", "");

        prompt.setResponseText(response);

        promptService.verifyPromptResponse(prompt);

        promptService.save(prompt);

        return response;
    }

    /**
     * Validates user's math expression request.
     *
     * @param request math expression to verify
     * @return true if expression is valid
     */
    public boolean useMathString(MathExpressionRequest request) {
        return promptService.verifyUserMathExpressionRequest(request);
    }
}