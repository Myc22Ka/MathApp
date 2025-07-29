package pl.myc22ka.mathapp.ai.ollama.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.ai.prompt.dto.PromptRequest;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;

@RestController
@RequestMapping("/ollama")
@RequiredArgsConstructor
public class OllamaController {

    private final OllamaService ollamaService;

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return ollamaService.chat(prompt);
    }

    @PostMapping("/generate-math-string")
    public String generateMathString(@RequestBody PromptRequest request) {
        return ollamaService.generateMathString(request);
    }
}
