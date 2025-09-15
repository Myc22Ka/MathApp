package pl.myc22ka.mathapp.step.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.step.service.WorkflowService;

@RestController
@RequestMapping("/steps")
@AllArgsConstructor
public class StepController {

    private final WorkflowService workflowService;

    @PostMapping("/execute/{exerciseId}")
    public String executeExercise(@PathVariable Long exerciseId) {
        workflowService.executeExercise(exerciseId);
        return "Executed exercise with id: " + exerciseId;
    }
}
