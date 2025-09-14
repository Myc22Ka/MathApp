package pl.myc22ka.mathapp.ai.ollama.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller responsible for communication with the Ollama AI service.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 05.08.2025
 */
@RestController
@RequestMapping("/ollama")
@RequiredArgsConstructor
@Tag(name = "Ollama AI", description = "Endpoints for communication with Ollama AI service")
public class OllamaController {

    private final OllamaService ollamaService;

    /**
     * Endpoint for sending a plain chat prompt to Ollama AI.
     *
     * @param prompt The message or question to send to the AI.
     * @return AI-generated response as plain text.
     */
    @Operation(summary = "Chat with AI", description = "Send a plain text prompt and get AI response")
    @GetMapping("/chat")
    public String chat(
            @Parameter(description = "Prompt text for the AI", required = true)
            @RequestParam String prompt
    ) {
        return ollamaService.chat(prompt);
    }

    /**
     * Endpoint for generating a math expression using Ollama AI based on structured input.
     *
     * @param request Request containing type, level, and rules for math expression generation.
     * @return AI-generated mathematical set expression as plain text.
     */
    @Operation(summary = "Generate math expression", description = "Generate mathematical set expression based on difficulty and constraints")
    @PostMapping("/generate-math-expression")
    public String generateMathExpression(@RequestBody MathExpressionChatRequest request) {
        return ollamaService.generatePrompt(request).getResponseText();
    }
}
