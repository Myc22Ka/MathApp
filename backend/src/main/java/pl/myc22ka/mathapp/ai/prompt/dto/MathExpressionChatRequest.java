package pl.myc22ka.mathapp.ai.prompt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;

import java.util.List;

/**
 * Request DTO for generating math expressions using AI.
 *
 * @param topicType type of mathematical expression to generate
 * @param modifiers list of modifiers to customize the expression
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 06.08.2025
 */
@Schema(description = "Request for generating mathematical expressions",
        example = """
        {
          "topicType": "SET",
          "modifiers": [
            {
              "type": "Difficulty",
              "difficultyLevel": 1
            },
            {
              "type": "Template",
              "template": "DISJOINT_SETS",
              "templateInformation": "(1,4)"
            }
          ]
        }
        """)
public record MathExpressionChatRequest(

        @Schema(description = "Type of mathematical expression",
                example = "SET")
        PromptType topicType,

        @Schema(description = "List of modifiers to customize the expression")
        List<ModifierRequest> modifiers
) {}
