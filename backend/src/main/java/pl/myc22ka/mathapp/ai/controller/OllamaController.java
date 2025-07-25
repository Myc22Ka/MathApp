package pl.myc22ka.mathapp.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.myc22ka.mathapp.ai.service.OllamaService;

@RestController
@RequestMapping("/ollama")
@RequiredArgsConstructor
public class OllamaController {

    private final OllamaService ollamaService;

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return ollamaService.chat(prompt);
    }
}
