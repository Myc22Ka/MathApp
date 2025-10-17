package pl.myc22ka.mathapp.modifier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.utils.resolver.component.TemplateResolver;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;
import pl.myc22ka.mathapp.topic.component.helper.TopicHelper;
import pl.myc22ka.mathapp.modifier.model.Modifier;
import pl.myc22ka.mathapp.topic.model.Topic;
import pl.myc22ka.mathapp.modifier.model.modifiers.Requirement;
import pl.myc22ka.mathapp.modifier.model.modifiers.Template;
import pl.myc22ka.mathapp.modifier.repository.ModifierRepository;
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
 * @author Myc22Ka
 * @version 1.0.3
 * @since 11.08.2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modifier definition for customizing generated mathematical expressions",
        example = """
                {
                  "type": "DIFFICULTY",
                  "difficultyLevel": 1
                }
                """)
public class ModifierRequest {

    private static final ExpressionFactory expressionFactory = new ExpressionFactory();

    @Schema(description = "Type of modifier", example = "DIFFICULTY")
    private String type;
    @Schema(description = "Difficulty level (required for type = DIFFICULTY)", example = "1")
    private Integer difficultyLevel;
    @Schema(description = "Requirement constraint (required for type = REQUIREMENT)", example = "INTERVALS_ONLY")
    private Requirement requirement;
    @Schema(description = "Template type (required for type = TEMPLATE)", example = "DISJOINT_SETS")
    private Template template;
    @Schema(description = "Optional extra information for the template (only for type = TEMPLATE)", example = "(1,4)")
    private String templateInformation;

    /**
     * Converts this request into a corresponding {@link Modifier} entity.
     * <p>
     * Looks up the modifier in the database based on topic and type.
     * For TEMPLATE type, optionally resolves templateInformation into a {@link ContextRecord}.
     *
     * @param topic        the topic associated with the modifier
     * @param repository   repository to fetch the modifier entity
     * @param topicHelper  helper for resolving template placeholders
     * @return the corresponding {@link Modifier} entity
     * @throws IllegalArgumentException if the type is unknown
     * @throws IllegalStateException    if a matching modifier cannot be found in the repository
     */
    public Modifier toModifier(@NotNull Topic topic, @NotNull ModifierRepository repository, @NotNull TopicHelper topicHelper) {
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

                if (templateInformation != null) {
                    var templateString = new TemplateResolver(repository, topicHelper).findTemplatePrefixes(modifier.getModifierText());

                    modifier.setInformation(
                            expressionFactory.parse(
                                    new ContextRecord(templateString.toString(), topic.getType(), templateInformation)
                            )
                    );
                }

                yield modifier;
            }
            default -> throw new IllegalArgumentException("Unknown modifier type: " + type);
        };
    }

    /**
     * Returns a new ModifierRequest with updated template information.
     * <p>
     * Useful for resolving placeholders in TEMPLATE type modifiers.
     *
     * @param newInfo new value for templateInformation
     * @return new ModifierRequest with updated templateInformation
     */
    public ModifierRequest withTemplateInformation(String newInfo) {
        return new ModifierRequest(
                this.type,
                this.difficultyLevel,
                this.requirement,
                this.template,
                newInfo
        );
    }
}
