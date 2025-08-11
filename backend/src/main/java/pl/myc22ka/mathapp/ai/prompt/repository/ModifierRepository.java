package pl.myc22ka.mathapp.ai.prompt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.ai.prompt.model.Topic;
import pl.myc22ka.mathapp.ai.prompt.model.modifiers.*;

import java.util.Optional;

/**
 * Repository for managing Modifier entities and their subclasses.
 * Provides custom queries to find modifiers by topic and specific attributes.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 11.08.2025
 */
@Repository
public interface ModifierRepository extends JpaRepository<Modifier, Long> {

    /**
     * Find DifficultyModifier by topic and difficulty level.
     *
     * @param topic           the topic
     * @param difficultyLevel the difficulty level
     * @return optional DifficultyModifier
     */
    @Query("SELECT m FROM DifficultyModifier m WHERE m.topic = :topic AND m.difficultyLevel = :difficultyLevel")
    Optional<DifficultyModifier> findByTopicAndDifficultyLevel(
            @Param("topic") Topic topic,
            @Param("difficultyLevel") Integer difficultyLevel
    );

    /**
     * Find RequirementModifier by topic and requirement.
     *
     * @param topic       the topic
     * @param requirement the requirement
     * @return optional RequirementModifier
     */
    @Query("SELECT m FROM RequirementModifier m WHERE m.topic = :topic AND m.requirement = :requirement")
    Optional<RequirementModifier> findByTopicAndRequirement(
            @Param("topic") Topic topic,
            @Param("requirement") Requirement requirement
    );

    /**
     * Find TemplateModifier by topic and template.
     *
     * @param topic    the topic
     * @param template the template
     * @return optional TemplateModifier
     */
    @Query("SELECT t FROM TemplateModifier t WHERE t.topic = :topic AND t.template = :template")
    Optional<TemplateModifier> findByTopicAndTemplate(
            @Param("topic") Topic topic,
            @Param("template") Template template
    );

    /**
     * Find maximum difficulty level for a given topic.
     *
     * @param topic the topic
     * @return optional max difficulty level
     */
    @Query("SELECT MAX(d.difficultyLevel) FROM DifficultyModifier d WHERE d.topic = :topic")
    Optional<Integer> findMaxDifficultyLevelByTopic(@Param("topic") Topic topic);
}