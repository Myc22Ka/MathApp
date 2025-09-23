package pl.myc22ka.mathapp.step.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.step.dto.StepDTO;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.model.StepType;
import pl.myc22ka.mathapp.step.repository.StepDefinitionRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StepService {

    private final StepDefinitionRepository stepDefinitionRepository;

    public Page<StepDTO> getSteps(int page, int size, StepType stepType,
                                  PromptType category, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<StepDefinition> stepDefinitions;

        if (stepType != null) {
            stepDefinitions = stepDefinitionRepository.findByStepType(stepType, pageable);
        } else if (category != null) {
            List<StepType> stepTypesForCategory = Arrays.stream(StepType.values())
                    .filter(type -> type.getCategory() == category)
                    .toList();
            stepDefinitions = stepDefinitionRepository.findByStepTypeIn(stepTypesForCategory, pageable);
        } else {
            stepDefinitions = stepDefinitionRepository.findAll(pageable);
        }

        return stepDefinitions.map(StepDTO::fromStepDefinition);
    }
}
