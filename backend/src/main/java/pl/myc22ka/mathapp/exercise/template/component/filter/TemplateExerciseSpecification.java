package pl.myc22ka.mathapp.exercise.template.component.filter;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Specification builder for {@link TemplateExercise} entity.
 * <p>
 * Provides JPA {@link Specification}s for filtering TemplateExercise entities
 * by difficulty and category.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
public class TemplateExerciseSpecification {

    /**
     * Creates a Specification to filter TemplateExercise by difficulty.
     *
     * @param difficulty the difficulty level to filter by (nullable)
     * @return Specification matching the given difficulty, or null if difficulty is null/empty
     */
    @NotNull
    private static Specification<TemplateExercise> hasDifficulty(String difficulty) {
        return (root, query, criteriaBuilder) -> {
            if (difficulty == null || difficulty.trim().isEmpty()) return null;

            return criteriaBuilder.equal(root.get("difficulty"), difficulty);
        };
    }

    /**
     * Creates a Specification to filter TemplateExercise by category.
     *
     * @param category the category (TemplatePrefix) to filter by (nullable)
     * @return Specification matching the given category, or null if category is null
     */
    @NotNull
    private static Specification<TemplateExercise> hasCategory(TemplatePrefix category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) return null;

            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    /**
     * Combines difficulty and category filters into a single Specification.
     *
     * @param difficulty the difficulty level to filter by (nullable)
     * @param category   the category to filter by (nullable)
     * @return Specification matching both filters
     */
    @NotNull
    public static Specification<TemplateExercise> withFilters(String difficulty, TemplatePrefix category) {
        return hasDifficulty(difficulty)
                .and(hasCategory(category));
    }
}
