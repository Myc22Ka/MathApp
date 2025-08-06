package pl.myc22ka.mathapp.ai.prompt.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Requirement;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Template;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;

public record ModifierRequest(
        String type,
        Integer difficultyLevel,
        Requirement requirement,
        Template template,
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
