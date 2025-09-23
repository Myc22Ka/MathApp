package pl.myc22ka.mathapp.step.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.step.dto.StepDTO;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.service.StepService;

@RestController
@RequestMapping("/steps")
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;

    @GetMapping
    public Page<StepDTO> getSteps(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) StepType stepType,
            @RequestParam(required = false) PromptType category,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        return stepService.getSteps(page, size, stepType, category, sortBy, sortDirection);
    }
}
