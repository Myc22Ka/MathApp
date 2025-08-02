package pl.myc22ka.mathapp.ai.ollama.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.myc22ka.mathapp.ai.prompt.dto.PromptRequest;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.ai.prompt.service.PromptService;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.set.ISet;
import pl.myc22ka.mathapp.model.set.Set;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OllamaService {

    @Value("${spring.ollama.base-url}")
    private String baseUrl;

    @Value("${spring.ollama.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExpressionFactory expressionFactory = new ExpressionFactory();
    private final PromptService promptService;

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

    public String generateMathString(PromptRequest request) {
        Prompt prompt = promptService.createPrompt(request);

        String response = chat(prompt.getFinalPromptText()).replace("\n", "").replaceFirst("^\\+", "");

        System.out.println(response);

        var result = expressionFactory.parse(response);

        prompt.setResponseText(result.toString());

        promptService.verifyPromptResponse(prompt, result);

        promptService.save(prompt);

        return response;
    }
}