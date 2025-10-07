package pl.myc22ka.mathapp.ai.prompt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.prompt.component.filter.ModifierSpecifications;
import pl.myc22ka.mathapp.ai.prompt.dto.ModifierDTO;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.repository.ModifierRepository;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Service for handling Modifier-related operations.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 11.08.2025
 */
@Service
@RequiredArgsConstructor
public class ModifierService {

    private final ModifierRepository modifierRepository;

    /**
     * Returns the maximum difficulty level for a given topic.
     * Defaults to 1 if none found.
     *
     * @param topic the topic
     * @return max difficulty level
     */
    public int getMaxDifficultyLevel(Topic topic) {
        return modifierRepository.findMaxDifficultyLevelByTopic(topic).orElse(1);
    }

    public Page<ModifierDTO> getModifiers(int page, int size,
                                          String sortBy,
                                          String sortDirection,
                                          TemplatePrefix category) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Modifier> spec = ModifierSpecifications.buildSpecification(category);

        return ModifierDTO.fromPage(modifierRepository.findAll(spec, pageable));
    }
}
