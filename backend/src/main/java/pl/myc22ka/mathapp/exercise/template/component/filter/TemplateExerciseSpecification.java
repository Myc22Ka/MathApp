package pl.myc22ka.mathapp.exercise.template.component.filter;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

public class TemplateExerciseSpecification {

    @NotNull
    public static Specification<TemplateExercise> hasDifficulty(String difficulty) {
        return (root, query, criteriaBuilder) -> {
            if (difficulty == null || difficulty.trim().isEmpty()) return null;

            return criteriaBuilder.equal(root.get("difficulty"), difficulty);
        };
    }

    @NotNull
    public static Specification<TemplateExercise> hasCategory(TemplatePrefix category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) return null;

            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    @NotNull
    public static Specification<TemplateExercise> withFilters(String difficulty, TemplatePrefix category) {
        return hasDifficulty(difficulty)
                .and(hasCategory(category));
    }
}
