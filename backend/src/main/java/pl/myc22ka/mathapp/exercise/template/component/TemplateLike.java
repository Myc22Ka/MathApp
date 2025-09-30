package pl.myc22ka.mathapp.exercise.template.component;

import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;

public interface TemplateLike {
    PromptType getCategory();
    String getDifficulty();
    String getTemplateText();
    String getTemplateAnswer();
    String getClearText();
    List<StepWrapper> getSteps();
    Long getExerciseCounter();
}
