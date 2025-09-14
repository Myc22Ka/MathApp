package pl.myc22ka.mathapp.step.service;

import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.step.model.Step;
import pl.myc22ka.mathapp.step.repository.StepRepository;

import java.util.List;

@Service
public class WorkflowService {

    private final StepRepository stepRepository;
    private final StepExecutorRegistry registry;

    public WorkflowService(StepRepository stepRepository, StepExecutorRegistry registry) {
        this.stepRepository = stepRepository;
        this.registry = registry;
    }

    public void executeExercise(Long exerciseId) {
        List<Step> steps = stepRepository.findByExerciseIdOrderByOrderIndex(exerciseId);

        for (Step step : steps) {
            registry.executeStep(step);
        }
    }
}
