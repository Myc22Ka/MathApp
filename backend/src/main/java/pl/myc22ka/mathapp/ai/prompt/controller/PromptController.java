package pl.myc22ka.mathapp.ai.prompt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionRequest;
import pl.myc22ka.mathapp.exceptions.DefaultResponse;

import java.time.Instant;

/**
 * Controller for handling prompt-related operations.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @see MathExpressionRequest
 * @see OllamaService
 * @since 01.10.2025
 */
@RestController
@RequestMapping("/prompt")
@RequiredArgsConstructor
@Tag(name = "Prompt", description = "Endpoints for prompt operations such as math expression verification")
public class PromptController {

    private final OllamaService ollamaService;

    /**
     * Verifies the correctness of a math expression provided in a request by applying modifiers
     * (like validation rules) to it based on the prompt topic type.
     *
     * @param request the math expression verification request containing the topic type,
     *                modifiers, and the user's response (expression).
     * @return 200 OK with verification success message if valid,
     * 400 BAD_REQUEST with error message if verification fails.
     */
    @Operation(
            summary = "Verify a math expression",
            description = "Checks whether a provided mathematical expression is correct according to the given topic and modifiers."
    )
    @PostMapping("/verify-math-expression")
    public ResponseEntity<DefaultResponse> useMathString(@RequestBody MathExpressionRequest request) {
        boolean verified = ollamaService.useMathString(request);

        return ResponseEntity.ok(
                new DefaultResponse(
                        Instant.now().toString(),
                        verified ? "The math expression was successfully verified." : "The math expression could not be verified.",
                        verified ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value()
                )
        );
    }
}
