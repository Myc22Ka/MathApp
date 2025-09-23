package pl.myc22ka.mathapp.step.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.step.model.StepDefinition;
import pl.myc22ka.mathapp.step.model.StepType;

import java.util.List;

@Repository
public interface StepDefinitionRepository extends JpaRepository<StepDefinition, Long> {

    Page<StepDefinition> findByStepType(StepType stepType, Pageable pageable);

    Page<StepDefinition> findByStepTypeIn(List<StepType> stepTypes, Pageable pageable);
}
