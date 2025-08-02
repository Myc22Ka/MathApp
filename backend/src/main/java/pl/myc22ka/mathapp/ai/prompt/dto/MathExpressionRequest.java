package pl.myc22ka.mathapp.ai.prompt.dto;

import pl.myc22ka.mathapp.ai.prompt.model.PromptType;

import java.util.List;

public record MathExpressionRequest(
        PromptType topicType,
        List<ModifierRequest> modifiers,
        String response
) {}
