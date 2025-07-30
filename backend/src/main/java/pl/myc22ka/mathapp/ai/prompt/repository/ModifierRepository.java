package pl.myc22ka.mathapp.ai.prompt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.DifficultyModifier;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.Requirement;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.RequirementModifier;

import java.util.Optional;

@Repository
public interface ModifierRepository extends JpaRepository<Modifier, Long> {

    @Query("SELECT m FROM DifficultyModifier m WHERE m.topic = :topic AND m.difficultyLevel = :difficultyLevel")
    Optional<DifficultyModifier> findByTopicAndDifficultyLevel(
            @Param("topic") Topic topic,
            @Param("difficultyLevel") Integer difficultyLevel
    );

    @Query("SELECT m FROM RequirementModifier m WHERE m.topic = :topic AND m.requirement = :requirement")
    Optional<RequirementModifier> findByTopicAndRequirement(
            @Param("topic") Topic topic,
            @Param("requirement") Requirement requirement
    );

    @Query("SELECT MAX(d.difficultyLevel) FROM DifficultyModifier d WHERE d.topic = :topic")
    Optional<Integer> findMaxDifficultyLevelByTopic(@Param("topic") Topic topic);
}