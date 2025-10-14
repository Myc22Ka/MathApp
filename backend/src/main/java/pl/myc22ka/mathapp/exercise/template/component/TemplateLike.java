package pl.myc22ka.mathapp.exercise.template.component;

import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.util.List;

public interface TemplateLike {
    TemplatePrefix getCategory();
    String getDifficulty();
    String getTemplateText();
    String getTemplateAnswer();
    String getClearText();
    List<StepWrapper> getSteps();
    Long getExerciseCounter();

    void setExerciseCounter(Long counter);
}
