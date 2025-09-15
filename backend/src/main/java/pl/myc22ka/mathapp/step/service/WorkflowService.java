package pl.myc22ka.mathapp.step.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.exercise.exercise.component.helper.ExerciseHelper;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.step.model.Step;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WorkflowService {

    private final StepExecutorRegistry registry;
    private final ExerciseHelper exerciseHelper;
    private final MemoryService memoryService;

    public void executeExercise(Long exerciseId) {
        Exercise exercise = exerciseHelper.getExercise(exerciseId);

        var context = exerciseHelper.deserializeContext(exercise.getContextJson());

        memoryService.clear();
        memoryService.putAll(context);

        List<PrefixValue> contextList = new ArrayList<>(memoryService.getMemory().values());

        for (Step step : exercise.getTemplateExercise().getSteps()) {
            registry.executeStep(step, contextList);
        }
    }
}
