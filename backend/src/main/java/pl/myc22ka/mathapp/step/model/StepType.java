package pl.myc22ka.mathapp.step.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;

import static pl.myc22ka.mathapp.ai.prompt.model.PromptType.*;

@Getter
@AllArgsConstructor
public enum StepType {
    SET_UNION("Łączenie zbiorów", SET),

    FUNCTION_DERIVATIVE("Obliczanie pochodnej", FUNCTION),

    WARMUP("Rozgrzewka", COMMON),
    EXERCISE("Właściwe ćwiczenie", COMMON),
    REST("Odpoczynek", COMMON);

    private final String description;
    private final PromptType category;
}