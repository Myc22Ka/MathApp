package pl.myc22ka.mathapp.exercise.variant.component.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.ai.prompt.component.TemplateResolver;
import pl.myc22ka.mathapp.exercise.template.component.helper.TemplateExerciseHelper;
import pl.myc22ka.mathapp.exercise.variant.dto.TemplateExerciseVariantInitDTO;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.exercise.variant.repository.TemplateExerciseVariantRepository;
import pl.myc22ka.mathapp.step.component.helper.StepExecutionHelper;
import pl.myc22ka.mathapp.step.model.StepWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@DependsOn("stepInitializer")
public class TemplateExerciseVariantInitializer {

    private final TemplateExerciseVariantRepository variantRepository;
    private final TemplateExerciseHelper templateExerciseHelper;
    private final StepExecutionHelper stepExecutionHelper;
    private final ObjectMapper objectMapper;
    private final TemplateResolver templateResolver;

    @PostConstruct
    public void init() throws IOException {
        System.out.println("[INIT] TemplateExerciseVariantInitializer");

        if (variantRepository.count() > 0) {
            System.out.println("[INIT] Template exercise variants already exist, skipping initialization.");
            return;
        }

        try (InputStream inputStream = new ClassPathResource(
                "data/static/exercises/template-exercise-variants.json"
        ).getInputStream()) {

            TemplateExerciseVariantInitDTO[] variantsArray =
                    objectMapper.readValue(inputStream, TemplateExerciseVariantInitDTO[].class);

            List<TemplateExerciseVariant> variants = new ArrayList<>();

            for (TemplateExerciseVariantInitDTO dto : variantsArray) {
                var templateExercise = templateExerciseHelper.getTemplate(dto.templateExerciseId());

                String cleanText = templateResolver.removeTemplatePlaceholders(dto.templateText());

                var variant = TemplateExerciseVariant.builder()
                        .templateExercise(templateExercise)
                        .category(dto.category())
                        .difficulty(dto.difficulty())
                        .templateText(dto.templateText())
                        .templateAnswer(dto.templateAnswer())
                        .clearText(cleanText)
                        .exerciseCounter(0L)
                        .steps(dto.steps() != null ? dto.steps() : new ArrayList<>())
                        .build();

                for (StepWrapper step : variant.getSteps()) {
                    step.setVariant(variant);

                    var definition = stepExecutionHelper.getStepDefinition(step.getStepDefinitionId());
                    step.setStepDefinition(definition);

                    if (step.getPrefixes() == null) {
                        step.setPrefixes(new ArrayList<>());
                    }
                }

                variants.add(variant);
            }

            variantRepository.saveAll(variants);
            System.out.println("[INIT] " + variants.size() + " template exercise variants saved.");
        }
    }
}
