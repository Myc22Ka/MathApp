package pl.myc22ka.mathapp.ai.prompt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierRequest;
import pl.myc22ka.mathapp.ai.prompt.handler.ModifierExecutor;
import pl.myc22ka.mathapp.ai.prompt.handler.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.TemplateModifier;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;
import pl.myc22ka.mathapp.ai.prompt.repository.PromptRepository;
import pl.myc22ka.mathapp.ai.prompt.repository.TopicRepository;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;
import pl.myc22ka.mathapp.model.expression.MathExpression;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PromptService {

    private final TopicRepository topicRepository;
    private final ModifierRepository modifierRepository;
    private final PromptRepository promptRepository;
    private final ModifierExecutor modifierExecutor;
    private final TemplateResolver templateResolver;

    public void save(Prompt prompt) {
        promptRepository.save(prompt);
    }

    public void verifyPromptResponse(@NotNull Prompt prompt, MathExpression response){
        boolean allVerified = true;

        for (Modifier modifier : prompt.getModifiers()) {
            boolean verified = modifierExecutor.applyModifier(modifier, prompt.getTopic().getType(), response);
            if (!verified) {
                allVerified = false;
            }
        }

        prompt.setVerified(allVerified);
    }

    public Prompt createPrompt(@NotNull MathExpressionChatRequest request) {
        Topic topic = findTopicByType(request.topicType());
        List<Modifier> modifiers = createOrFindModifiers(request.modifiers(), topic);

        if (!request.templateInformation().isEmpty()) {
            MathExpression parsed = new ExpressionFactory().parse(request.templateInformation());

            for (var modifier : modifiers) {
                if (modifier instanceof TemplateModifier t) {
                    t.setInformation(parsed); // zachowaj surowe info

                    // załaduj oryginalny tekst
                    String original = t.getModifierText();

                    // przygotuj kontekst dla resolvera (np. s → set/funkcja)
                    Map<String, Object> context = Map.of(
                            TemplatePrefix.SET.getKey(), parsed
                    );

                    // podmień ${s:} → parsed.toString()
                    String resolvedText = templateResolver.resolve(original, context);

                    // ustaw podmieniony tekst jako ostateczny modifierText
                    t.setModifierText(resolvedText);
                }
            }
        }

        Prompt prompt = Prompt.builder()
                .topic(topic)
                .modifiers(modifiers)
                .build();

        prompt.buildFinalPromptText();

        return prompt;
    }

    public Prompt createPrompt(@NotNull MathExpressionRequest request) {
        Topic topic = findTopicByType(request.topicType());
        List<Modifier> modifiers = createOrFindModifiers(request.modifiers(), topic);

        if(!request.templateInformation().isEmpty()) {
            for (var modifier : modifiers) {
                if(modifier instanceof TemplateModifier t){
                    var parsed = new ExpressionFactory().parse(request.templateInformation());

                    t.setInformation(parsed);
                }
            }
        }

        Prompt prompt = Prompt.builder()
                .topic(topic)
                .modifiers(modifiers)
                .build();

        prompt.setFinalPromptText(null);

        return prompt;
    }

    private Topic findTopicByType(PromptType topicType) {
        return topicRepository.findFirstByType(topicType)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nie znaleziono tematu dla typu: " + topicType));
    }

    private List<Modifier> createOrFindModifiers(List<ModifierRequest> modifierRequests, Topic topic) {
        if (modifierRequests == null || modifierRequests.isEmpty()) {
            return List.of();
        }

        return modifierRequests.stream()
                .map(req -> req.toModifier(topic, modifierRepository))
                .toList();
    }
}