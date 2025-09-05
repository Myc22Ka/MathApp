package pl.myc22ka.mathapp.exercise.exercise.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.ollama.service.OllamaService;
import pl.myc22ka.mathapp.ai.prompt.dto.MathExpressionChatRequest;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.exercise.exercise.component.helper.ExerciseHelper;
import pl.myc22ka.mathapp.exercise.exercise.component.helper.ValidationHelper;
import pl.myc22ka.mathapp.exercise.exercise.dto.ExerciseDTO;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final OllamaService ollamaService;
    private final ExerciseHelper exerciseHelper;
    private final ValidationHelper validationHelper;

    public ExerciseDTO createExercise(Long templateId, Long variantId, @NotNull List<String> values) {
        validationHelper.validateTemplateOrVariant(templateId, variantId);

        TemplateExercise template;
        if (templateId != null) {
            template = exerciseHelper.getTemplate(templateId);
        } else {
            TemplateExerciseVariant variant = exerciseHelper.getVariant(variantId);
            template = variant.getTemplateExercise();
        }

        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);
        exerciseHelper.validatePlaceholderCount(placeholders, values);

        List<PrefixValue> context = exerciseHelper.buildContext(placeholders, values);

        boolean allVerified = exerciseHelper.verifyPlaceholders(placeholders, values, context, template.getCategory());

        String finalText = exerciseHelper.resolveText(template, context);

        Exercise exercise = exerciseHelper.buildExercise(template, values, finalText);
        exercise.setVerified(allVerified);

        return ExerciseDTO.fromEntity(exerciseRepository.save(exercise));
    }

    public Optional<ExerciseDTO> findById(Long id) {
        return exerciseRepository.findById(id).map(ExerciseDTO::fromEntity);
    }

    public List<ExerciseDTO> findAll() {
        return exerciseRepository.findAll()
                .stream()
                .map(ExerciseDTO::fromEntity)
                .toList();
    }

    public void deleteById(Long id) {
        if (!exerciseRepository.existsById(id)) {
            throw new IllegalArgumentException("Exercise not found with id " + id);
        }
        exerciseRepository.deleteById(id);
    }

    public ExerciseDTO generateExercise(Long templateId, Long variantId) {
        validationHelper.validateTemplateOrVariant(templateId, variantId);

        TemplateExercise template;
        if (templateId != null) {
            template = exerciseHelper.getTemplate(templateId);
        } else {
            TemplateExerciseVariant variant = exerciseHelper.getVariant(variantId);
            template = variant.getTemplateExercise();
        }

        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);

        List<String> values = new ArrayList<>();
        List<PrefixValue> context = new ArrayList<>();

        for (var entry : placeholders) {
            String generatedText = ollamaService.generateMathExpression(
                    new MathExpressionChatRequest(
                            PromptType.valueOf(entry.prefix().name()),
                            entry.modifiers() == null ? new ArrayList<>() : entry.modifiers()
                    ).withContext(context)
            );

            String parsedText = exerciseHelper.parseValue(generatedText);
            values.add(parsedText);
            context.add(new PrefixValue(entry.prefix().getKey() + entry.index(), parsedText));
        }

        String finalText = exerciseHelper.resolveText(template, context);
        Exercise exercise = exerciseHelper.buildExercise(template, values, finalText);
        return ExerciseDTO.fromEntity(exerciseRepository.save(exercise));
    }

    public ExerciseDTO updateExercise(Long id, @NotNull List<String> values) {
        Exercise exercise = exerciseHelper.getExercise(id);
        TemplateExercise template = exerciseHelper.getTemplate(exercise.getTemplateExercise().getId());
        List<PrefixModifierEntry> placeholders = exerciseHelper.getPlaceholders(template);

        exerciseHelper.validatePlaceholderCount(placeholders, values);
        List<PrefixValue> context = exerciseHelper.buildContext(placeholders, values);

        boolean allVerified = exerciseHelper.verifyPlaceholders(placeholders, values, context, template.getCategory());

        String finalText = exerciseHelper.resolveText(template, context);

        exercise.setValues(values);
        exercise.setText(finalText);
        exercise.setVerified(allVerified);

        return ExerciseDTO.fromEntity(exerciseRepository.save(exercise));
    }
}