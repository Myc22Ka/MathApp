package pl.myc22ka.mathapp.ai.ollama.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;

@RestController
@RequestMapping("/ollama")
@RequiredArgsConstructor
public class OllamaController {

    private final OllamaService ollamaService;

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return ollamaService.chat(prompt);
    }

    @PostMapping("/generate-math-expression")
    public String generateMathExpression(@RequestBody MathExpressionChatRequest request) {
        return ollamaService.generateMathString(request);
    }
}
