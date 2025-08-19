package pl.myc22ka.mathapp.exercise.exercise.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.validator.TemplateResolver;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final TemplateResolver templateResolver;
    private final OllamaService ollamaService;

    public Exercise createExercise(Long templateId, List<String> values) {
        TemplateExercise template = templateExerciseRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with id " + templateId));

        Exercise exercise = Exercise.builder()
                .templateExercise(template)
                .values(values)
                .build();

        return exerciseRepository.save(exercise);
    }

    public Optional<Exercise> findById(Long id) {
        return exerciseRepository.findById(id);
    }

    public Exercise generateExercise(Long templateId) {
        TemplateExercise template = templateExerciseRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with id " + templateId));

        List<PrefixModifierEntry> placeholders = templateResolver.findPrefixModifiers(template.getTemplateText());

        List<String> values = new ArrayList<>();
        List<PrefixValue> context = new ArrayList<>(); // lista zamiast mapy

        for (var entry : placeholders) {
            TemplatePrefix prefix = entry.prefix();
            List<ModifierRequest> modifiers = entry.modifiers();

            if (modifiers == null) {
                modifiers = new ArrayList<>();
            }

            MathExpressionChatRequest request = new MathExpressionChatRequest(
                    PromptType.valueOf(prefix.name()),
                    modifiers
            );

            String generatedText = ollamaService.generateMathExpression(request);
            values.add(generatedText);

            context.add(new PrefixValue(prefix.getKey(), generatedText));
        }

        String finalText = templateResolver.resolve(template.getTemplateText(), context);

        Exercise exercise = Exercise.builder()
                .templateExercise(template)
                .values(values)
                .text(finalText)
                .build();

        return exerciseRepository.save(exercise);
    }
}
