package pl.myc22ka.mathapp.ai.prompt.dto;

import pl.myc22ka.mathapp.ai.prompt.model.PromptType;

import java.util.List;

public record MathExpressionChatRequest(
        PromptType topicType,
        List<ModifierRequest> modifiers,
        String templateInformation
) {}
