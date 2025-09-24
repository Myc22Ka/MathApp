package pl.myc22ka.mathapp.exercise.exercise.component.filter;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import pl.myc22ka.mathapp.ai.prompt.model.PromptType;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;

import java.util.ArrayList;
import java.util.List;

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
    public static Specification<Exercise> hasDifficultyOrCategory(String difficulty, PromptType category) {
        return (root, query, cb) -> {
            if ((difficulty == null || difficulty.trim().isEmpty()) && category == null) {
                return null;
            }

            Join<Exercise, TemplateExercise> templateJoin = root.join("templateExercise", JoinType.LEFT);
            Join<Exercise, TemplateExerciseVariant> variantJoin = root.join("templateExerciseVariant", JoinType.LEFT);

            List<Predicate> templatePredicates = new ArrayList<>();
            List<Predicate> variantPredicates = new ArrayList<>();

            if (difficulty != null && !difficulty.trim().isEmpty()) {
                templatePredicates.add(cb.equal(templateJoin.get("difficulty"), difficulty));
                variantPredicates.add(cb.equal(variantJoin.get("difficulty"), difficulty));
            }

            if (category != null) {
                templatePredicates.add(cb.equal(templateJoin.get("category"), category));
                variantPredicates.add(cb.equal(variantJoin.get("category"), category));
            }

            Predicate templateSpec = templatePredicates.isEmpty() ? null :
                    cb.and(templatePredicates.toArray(new Predicate[0]));
            Predicate variantSpec = variantPredicates.isEmpty() ? null :
                    cb.and(variantPredicates.toArray(new Predicate[0]));

            if (templateSpec != null && variantSpec != null) {
                return cb.or(templateSpec, variantSpec);
            } else if (templateSpec != null) {
                return templateSpec;
            } else {
                return variantSpec;
            }
        };
    }

    @NotNull
    public static Specification<Exercise> withFilters(Double rating, String difficulty, PromptType category) {
        return hasRating(rating)
                .and(hasDifficultyOrCategory(difficulty, category));
    }
}