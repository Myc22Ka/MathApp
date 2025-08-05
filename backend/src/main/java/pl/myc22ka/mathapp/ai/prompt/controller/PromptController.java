package pl.myc22ka.mathapp.ai.prompt.controller;

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

@RestController
@RequestMapping("/prompt")
@RequiredArgsConstructor
public class PromptController {

    private final OllamaService ollamaService;

    @PostMapping("/verify-math-expression")
    public ResponseEntity<DefaultResponse> useMathString(@RequestBody MathExpressionRequest request) {

        boolean verified = ollamaService.useMathString(request);

        String message = verified
                ? "The math expression was successfully verified."
                : "The math expression could not be verified.";

        return ResponseEntity.ok(
                new DefaultResponse(
                        Instant.now().toString(),
                        message,
                        HttpStatus.OK.value()
                )
        );
    }
}
