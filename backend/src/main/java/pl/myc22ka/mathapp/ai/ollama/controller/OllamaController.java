package pl.myc22ka.mathapp.ai.ollama.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;

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
public class OllamaController {

    private final OllamaService ollamaService;

    /**
     * Endpoint for sending a plain chat prompt to Ollama AI.
     *
     * @param prompt The message or question to send to the AI.
     * @return AI-generated response as plain text.
     */
    @Operation(summary = "Chat with Ollama AI", description = "Sends a plain text prompt to the AI and returns the response.")
    @GetMapping("/chat")
    public String chat(
            @Parameter(description = "Plain prompt text for the AI", required = true)
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
    @Operation(summary = "Generate a math expression", description = "Generates a mathematical set expression based on difficulty and constraints.")
    @PostMapping("/generate-math-expression")
    public String generateMathExpression(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body with details for math expression generation",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MathExpressionChatRequest.class))
            )
            @RequestBody MathExpressionChatRequest request
    ) {
        return ollamaService.generateMathExpression(request);
    }
}
