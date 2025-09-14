package pl.myc22ka.mathapp.step.controller;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.step.dto.StepDTO;
import pl.myc22ka.mathapp.step.model.Step;
import pl.myc22ka.mathapp.step.service.StepExecutorRegistry;
import pl.myc22ka.mathapp.step.service.WorkflowService;

import java.util.List;

@RestController
@RequestMapping("/steps")
@AllArgsConstructor
public class StepController {

    private final StepExecutorRegistry stepExecutorRegistry;
    private final WorkflowService workflowService;

    @PostMapping("/execute/{exerciseId}")
    public String executeExercise(@PathVariable Long exerciseId) {
        workflowService.executeExercise(exerciseId);
        return "Executed exercise with id: " + exerciseId;
    }

    @PostMapping("/execute/step")
    public String executeStep(@NotNull @RequestBody StepDTO dto) {
        Step step = dto.toEntityForTemplate(null);
        stepExecutorRegistry.executeStep(step);
        return "Executed step: " + dto.stepType();
    }

    @PostMapping("/execute")
    public String executeSteps(@NotNull @RequestBody List<StepDTO> dtos) {
        for (StepDTO dto : dtos) {
            Step step = dto.toEntityForTemplate(null);
            stepExecutorRegistry.executeStep(step);
        }
        return "Executed steps: " + dtos.stream().map(StepDTO::stepType).toList();
    }
}
