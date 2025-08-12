package pl.myc22ka.mathapp.ai.prompt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Requirement;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Template;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;

/**
 * Request DTO for specifying a modifier when generating math expressions.
 * <p>
 * Depending on the {@code type}, only some fields will be relevant:
 * <ul>
 *   <li>{@code DIFFICULTY} → requires {@code difficultyLevel}</li>
 *   <li>{@code REQUIREMENT} → requires {@code requirement}</li>
 *   <li>{@code TEMPLATE} → requires {@code template} and optionally {@code templateInformation}</li>
 * </ul>
 *
 * @param type                type of the modifier (DIFFICULTY, REQUIREMENT, TEMPLATE)
 * @param difficultyLevel     difficulty level (only for type = DIFFICULTY)
 * @param requirement         specific requirement (only for type = REQUIREMENT)
 * @param template            template type (only for type = TEMPLATE)
 * @param templateInformation optional extra information for the template (e.g., an expression)
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Schema(description = "Modifier definition for customizing generated mathematical expressions",
        example = """
        {
          "type": "DIFFICULTY",
          "difficultyLevel": 1
        }
        """)
public record ModifierRequest(

        @Schema(description = "Type of modifier", example = "DIFFICULTY")
        String type,

        @Schema(description = "Difficulty level (required for type = DIFFICULTY)", example = "1")
        Integer difficultyLevel,

        @Schema(description = "Requirement constraint (required for type = REQUIREMENT)", example = "INTERVALS_ONLY")
        Requirement requirement,

        @Schema(description = "Template type (required for type = TEMPLATE)", example = "DISJOINT_SETS")
        Template template,

        @Schema(description = "Optional extra information for the template (only for type = TEMPLATE)", example = "(1,4)")
        String templateInformation
) {

    private static final ExpressionFactory expressionFactory = new ExpressionFactory();

    public Modifier toModifier(@NotNull Topic topic, @NotNull ModifierRepository repository) {
        return switch (type.toUpperCase()) {
            case "DIFFICULTY" -> repository
                    .findByTopicAndDifficultyLevel(topic, difficultyLevel)
                    .orElseThrow(() -> new IllegalStateException("No DifficultyModifier found for topic=" + topic.getId() + " and level=" + difficultyLevel));
            case "REQUIREMENT" -> repository
                    .findByTopicAndRequirement(topic, requirement)
                    .orElseThrow(() -> new IllegalStateException("No RequirementModifier found for topic=" + topic.getId() + " and requirement=" + requirement));
            case "TEMPLATE" -> {
                var modifier = repository
                        .findByTopicAndTemplate(topic, template)
                        .orElseThrow(() -> new IllegalStateException("No TemplateModifier found for topic=" + topic.getId() + " and template=" + template));

                if (modifier instanceof TemplateModifier templateModifier && templateInformation != null) {
                    templateModifier.setInformation(expressionFactory.parse(templateInformation));
                }

                yield modifier;
            }
            default -> throw new IllegalArgumentException("Unknown modifier type: " + type);
        };
    }
}
