package pl.myc22ka.mathapp.ai.prompt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.modifier.dto.ModifierRequest;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;

import java.util.List;

/**
 * Request DTO for generating math expressions using AI.
 *
 * @param topicType type of mathematical expression to generate
 * @param modifiers list of modifiers to customize the expression
 *
 * @author Myc22Ka
 * @version 1.0.5
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
        TemplatePrefix topicType,

        @Schema(description = "List of modifiers to customize the expression")
        List<ModifierRequest> modifiers
) {

        /**
         * Finishes process of resolving template modifiers using provided context.
         * <p>
         * Used when generating expressions that depend on results of previous ones.
         * For example, a template modifier may reference a value generated earlier.
         *
         * @param context list of previously generated context records
         * @return a new {@link MathExpressionChatRequest} with resolved modifier information
         */
        public MathExpressionChatRequest withContext(List<ContextRecord> context) {
                if (modifiers == null) {
                        return this;
                }

                List<ModifierRequest> resolvedModifiers = modifiers.stream()
                        .map(m -> {
                                if ("TEMPLATE".equalsIgnoreCase(m.getType()) && m.getTemplateInformation() != null) {
                                        String placeholderKey = m.getTemplateInformation();

                                        String replacement = context.stream()
                                                .filter(c -> c.key().templateString().equals(placeholderKey))
                                                .map(ContextRecord::value)
                                                .findFirst()
                                                .orElse(m.getTemplateInformation());

                                        return m.withTemplateInformation(replacement);
                                }
                                return m;
                        })
                        .toList();

                return new MathExpressionChatRequest(topicType, resolvedModifiers);
        }
}
