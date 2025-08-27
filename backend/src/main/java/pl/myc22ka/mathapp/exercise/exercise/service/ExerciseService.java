package pl.myc22ka.mathapp.exercise.exercise.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.ai.prompt.model.Prompt;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.service.PromptService;
import pl.myc22ka.mathapp.ai.prompt.validator.TemplateResolver;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseDTO;
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
    private final PromptService promptService;

    public ExerciseDTO createExercise(Long templateId, List<String> values) {
        TemplateExercise template = templateExerciseRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with id " + templateId));

        Exercise exercise = Exercise.builder()
                .templateExercise(template)
                .values(values)
                .build();

        Exercise saved = exerciseRepository.save(exercise);
        return ExerciseDTO.fromEntity(saved);
    }

    public Optional<ExerciseDTO> findById(Long id) {
        return exerciseRepository.findById(id)
                .map(ExerciseDTO::fromEntity);
    }

    public ExerciseDTO generateExercise(Long templateId) {
        TemplateExercise template = templateExerciseRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with id " + templateId));

        List<PrefixModifierEntry> placeholders = templateResolver.findPrefixModifiers(template.getTemplateText());

        List<String> values = new ArrayList<>();
        List<PrefixValue> context = new ArrayList<>();

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

            String generatedText = ollamaService.generateMathExpression(request.withContext(context));
            values.add(generatedText);

            context.add(new PrefixValue(prefix.getKey() + entry.index(), generatedText));
        }

        String finalText = templateResolver.resolve(template.getTemplateText(), context);

        Exercise exercise = Exercise.builder()
                .templateExercise(template)
                .values(values)
                .text(finalText)
                .build();

        Exercise saved = exerciseRepository.save(exercise);
        return ExerciseDTO.fromEntity(saved);
    }
}