package pl.myc22ka.mathapp.step.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;

import static pl.myc22ka.mathapp.ai.prompt.model.PromptType.*;

@Getter
@AllArgsConstructor
public enum StepType {
    SET_UNION(SET),
    SET_INTERSECTION(SET),
    SET_DIFFERENCE(SET),
    SET_FIND_ALL_INTEGERS_IN_RANGE(SET),
    SET_COMPLEMENT(SET),

    FUNCTION_DERIVATIVE(FUNCTION),

    WARMUP(COMMON),
    EXERCISE(COMMON),
    REST(COMMON);

    private final PromptType category;
}