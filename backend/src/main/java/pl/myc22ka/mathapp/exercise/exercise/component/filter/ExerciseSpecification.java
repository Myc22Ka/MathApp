package pl.myc22ka.mathapp.exercise.exercise.component.filter;

import jakarta.persistence.criteria.Join;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;

public class ExerciseSpecification {

    @NotNull
    @Contract(pure = true)
    public static Specification<Exercise> hasRating(Double rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null) return null;

            return criteriaBuilder.equal(root.get("rating"), rating);
        };
    }

    @NotNull
    @Contract(pure = true)
    public static Specification<Exercise> hasDifficulty(String difficulty) {
        return (root, query, criteriaBuilder) -> {
            if (difficulty == null || difficulty.trim().isEmpty()) return null;

            Join<Exercise, TemplateExercise> templateJoin = root.join("templateExercise");
            return criteriaBuilder.equal(templateJoin.get("difficulty"), difficulty);
        };
    }

    @NotNull
    @Contract(pure = true)
    public static Specification<Exercise> hasCategory(PromptType category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) return null;

            Join<Exercise, TemplateExercise> templateJoin = root.join("templateExercise");
            return criteriaBuilder.equal(templateJoin.get("category"), category);
        };
    }

    @NotNull
    public static Specification<Exercise> withFilters(Double rating, String difficulty, PromptType category) {
        return hasRating(rating)
                .and(hasDifficulty(difficulty))
                .and(hasCategory(category));
    }
}
