package pl.myc22ka.mathapp.exercise.exercise.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixModifierEntry;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.ai.prompt.service.PromptService;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.exercise.repository.ExerciseRepository;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.template.repository.TemplateExerciseRepository;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.repository.TemplateExerciseVariantRepository;
import pl.myc22ka.mathapp.model.expression.ExpressionFactory;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExerciseHelper {

    private final ExerciseRepository exerciseRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final TemplateResolver templateResolver;
    private final ExpressionFactory expressionFactory;
    private final TemplateExerciseVariantRepository templateVariantRepository;
    private final PromptService promptService;

    public TemplateExercise getTemplate(Long templateId) {
        return templateExerciseRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with id " + templateId));
    }

    public TemplateExerciseVariant getVariant(Long variantId) {
        return templateVariantRepository.findById(variantId)
                .orElseThrow(() -> new IllegalArgumentException("Variant not found with id " + variantId));
    }

    public Exercise getExercise(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id " + id));
    }

    public List<PrefixModifierEntry> getPlaceholders(@NotNull TemplateExercise template) {
        return templateResolver.findPrefixModifiers(template.getTemplateText());
    }

    public void validatePlaceholderCount(@NotNull List<PrefixModifierEntry> placeholders, @NotNull List<String> values) {
        if (placeholders.size() != values.size()) {
            throw new IllegalArgumentException("Number of values does not match number of placeholders in template");
        }
    }

    public List<PrefixValue> buildContext(@NotNull List<PrefixModifierEntry> placeholders, List<String> values) {
        List<PrefixValue> context = new ArrayList<>();
        for (int i = 0; i < placeholders.size(); i++) {
            PrefixModifierEntry entry = placeholders.get(i);
            String parsedText = parseValue(values.get(i));
            values.set(i, parsedText);
            context.add(new PrefixValue(entry.prefix().getKey() + entry.index(), parsedText));
        }
        return context;
    }

    public String parseValue(String value) {
        return expressionFactory.parse(value).toString();
    }

    public String resolveText(@NotNull TemplateExercise template, List<PrefixValue> context) {
        return templateResolver.resolve(template.getTemplateText(), context);
    }

    public Exercise buildExercise(TemplateExercise template, List<String> values, String text) {
        return Exercise.builder()
                .templateExercise(template)
                .values(values)
                .text(text)
                .build();
    }

    public boolean verifyPlaceholders(
            @NotNull List<PrefixModifierEntry> placeholders,
            @NotNull List<String> values,
            @NotNull List<PrefixValue> context,
            @NotNull PromptType category
    ) {
        boolean allVerified = true;

        for (int i = 0; i < placeholders.size(); i++) {
            var placeholder = placeholders.get(i);
            String currentValue = values.get(i);

            // Podmień templateInformation
            for (var modifier : placeholder.modifiers()) {
                if (modifier.getTemplateInformation() != null) {
                    var resolved = templateResolver.replaceTemplatePlaceholders(
                            "${" + modifier.getTemplateInformation() + "}", context
                    );
                    modifier.setTemplateInformation(resolved);
                }
            }

            // Weryfikacja
            boolean verified = promptService.verifyModifierRequestsWithValue(
                    placeholder.modifiers(),
                    currentValue,
                    category
            );

            if (!verified) {
                allVerified = false;
                break;
            }
        }

        return allVerified;
    }
}


