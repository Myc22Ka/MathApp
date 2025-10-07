package pl.myc22ka.mathapp.exercise.variant.component.filter;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

public class TemplateExerciseVariantSpecification {

    @NotNull
    public static Specification<TemplateExerciseVariant> hasDifficulty(String difficulty) {
        return (root, query, criteriaBuilder) -> {
            if (difficulty == null || difficulty.trim().isEmpty()) return null;

            return criteriaBuilder.equal(root.get("difficulty"), difficulty);
        };
    }

    @NotNull
    public static Specification<TemplateExerciseVariant> hasCategory(TemplatePrefix category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) return null;

            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    @NotNull
    public static Specification<TemplateExerciseVariant> withFilters(String difficulty, TemplatePrefix category) {
        return hasDifficulty(difficulty)
                .and(hasCategory(category));
    }
}
