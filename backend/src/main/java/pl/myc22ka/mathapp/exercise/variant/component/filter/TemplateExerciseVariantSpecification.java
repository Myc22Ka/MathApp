package pl.myc22ka.mathapp.exercise.variant.component.filter;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Provides JPA {@link Specification} utilities for filtering {@link TemplateExerciseVariant} entities
 * based on difficulty and category.
 * <p>
 * These specifications can be combined to build dynamic queries with optional filters.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 13.09.2025
 */
public class TemplateExerciseVariantSpecification {

    /**
     * Returns a specification that filters variants by difficulty.
     *
     * @param difficulty the difficulty string to filter by; null or empty string disables this filter
     * @return specification for difficulty filter
     */
    @NotNull
    private static Specification<TemplateExerciseVariant> hasDifficulty(String difficulty) {
        return (root, query, criteriaBuilder) -> {
            if (difficulty == null || difficulty.trim().isEmpty()) return null;

            return criteriaBuilder.equal(root.get("difficulty"), difficulty);
        };
    }

    /**
     * Returns a specification that filters variants by category.
     *
     * @param category the template prefix (category) to filter by; null disables this filter
     * @return specification for category filter
     */
    @NotNull
    private static Specification<TemplateExerciseVariant> hasCategory(TemplatePrefix category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) return null;

            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    /**
     * Returns a specification that filters variants by category.
     *
     * @param category the template prefix (category) to filter by; null disables this filter
     * @return specification for category filter
     */
    @NotNull
    public static Specification<TemplateExerciseVariant> withFilters(String difficulty, TemplatePrefix category) {
        return hasDifficulty(difficulty)
                .and(hasCategory(category));
    }
}
