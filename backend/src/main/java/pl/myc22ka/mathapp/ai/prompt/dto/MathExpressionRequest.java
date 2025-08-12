package pl.myc22ka.mathapp.ai.prompt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;

import java.util.List;

/**
 * Request DTO for generating math expressions using user input.
 *
 * @param topicType type of mathematical expression to generate
 * @param modifiers list of modifiers to customize the expression
 * @param response  given by the user expression input
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Schema(description = "Request for generating mathematical expressions",
        example = """
        {
          "topicType": "SET",
          "response": "(1,4)",
          "modifiers": [
            {
              "type": "Difficulty",
              "difficultyLevel": 1
            },
            {
              "type": "Requirement",
              "requirement": "INTERVALS_ONLY"
            }
          ]
        }
        """)
public record MathExpressionRequest(

        @Schema(description = "Type of mathematical expression",
                example = "SET")
        PromptType topicType,

        @Schema(description = "List of modifiers to customize the expression")
        List<ModifierRequest> modifiers,

        @Schema(description = "Expected answer or given expression",
                example = "(1,4)")
        String response
) {}