package pl.myc22ka.mathapp.exercise.exercise.component.filter;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import pl.myc22ka.mathapp.exercise.exercise.model.Exercise;
import pl.myc22ka.mathapp.exercise.template.model.TemplateExercise;
import pl.myc22ka.mathapp.exercise.variant.model.TemplateExerciseVariant;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.model.UserExercise;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specifications for filtering {@link Exercise} entities.
 * <p>
 * Provides reusable predicates to filter exercises by rating, template ID, difficulty, or category.
 * Can be combined using {@link Specification#and(Specification)} or {@link Specification#allOf(Specification[])}.
 *
 * @author Myc22Ka
 * @since 2025-10-17
 */
public class ExerciseSpecification {

    /**
     * Filter exercises by exact rating.
     *
     * @param rating rating value to filter by
     * @return JPA specification for rating
     */
    @NotNull
    private static Specification<Exercise> hasRating(Double rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null) return criteriaBuilder.conjunction();

            return criteriaBuilder.equal(root.get("rating"), rating);
        };
    }

    /**
     * Filter exercises by template ID.
     * <p>
     * Matches both direct template exercises and template exercise variants.
     *
     * @param templateId template ID to filter by
     * @return JPA specification for template ID
     */
    @NotNull
    private static Specification<Exercise> hasTemplateId(Long templateId) {
        return (root, query, cb) -> {
            if (templateId == null) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            // Przypadek 1: Exercise.templateExercise.id = templateId
            Join<Exercise, TemplateExercise> templateJoin = root.join("templateExercise", JoinType.LEFT);
            predicates.add(cb.equal(templateJoin.get("id"), templateId));

            // Przypadek 2: Exercise.templateExerciseVariant.templateExercise.id = templateId
            Join<Exercise, TemplateExerciseVariant> variantJoin = root.join("templateExerciseVariant", JoinType.LEFT);
            Join<TemplateExerciseVariant, TemplateExercise> variantTemplateJoin =
                    variantJoin.join("templateExercise", JoinType.LEFT);
            predicates.add(cb.equal(variantTemplateJoin.get("id"), templateId));

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    @NotNull
    private static Specification<Exercise> filterByUserLevel(Integer userLevel, Boolean onlyUserLevel) {
        return (root, query, cb) -> {
            if (userLevel == null || onlyUserLevel == null || !onlyUserLevel) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            Join<Exercise, TemplateExercise> templateJoin = root.join("templateExercise", JoinType.LEFT);
            predicates.add(cb.lessThanOrEqualTo(templateJoin.get("requiredLevel"), userLevel));

            Join<Exercise, TemplateExerciseVariant> variantJoin = root.join("templateExerciseVariant", JoinType.LEFT);
            predicates.add(cb.lessThanOrEqualTo(variantJoin.get("requiredLevel"), userLevel));

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    @NotNull
    public static Specification<Exercise> isSolvedByUser(Long userId, Boolean solvedFilter) {
        return (root, query, cb) -> {
            if (userId == null || solvedFilter == null) {
                return cb.conjunction();
            }

            Join<Exercise, UserExercise> userExerciseJoin = root.join("userExercises", JoinType.LEFT);

            userExerciseJoin.on(cb.equal(userExerciseJoin.get("user").get("id"), userId));

            if (solvedFilter) {
                return cb.and(
                        cb.isNotNull(userExerciseJoin.get("id")),
                        cb.equal(userExerciseJoin.get("solved"), true)
                );
            } else {
                return cb.isNull(userExerciseJoin.get("id"));
            }
        };
    }

    /**
     * Filter exercises by difficulty and/or category.
     *
     * @param difficulty difficulty level (optional)
     * @param category   template prefix category (optional)
     * @return JPA specification for difficulty or category
     */
    @NotNull
    private static Specification<Exercise> hasDifficultyOrCategory(String difficulty, TemplatePrefix category) {
        return (root, query, cb) -> {
            if ((difficulty == null || difficulty.trim().isEmpty()) && category == null) {
                return cb.conjunction();
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
            } else if (variantSpec != null) {
                return variantSpec;
            } else {
                return cb.conjunction();
            }
        };
    }

    /**
     * Combines all filters: rating, difficulty/category, and template ID.
     *
     * @param rating     rating to filter by
     * @param difficulty difficulty level
     * @param category   template category
     * @param templateId template ID
     * @return combined JPA specification
     */
    @NotNull
    public static Specification<Exercise> withFilters(Double rating, String difficulty,
                                                      TemplatePrefix category, Long templateId,
                                                      @NotNull User user, Boolean solvedFilter,
                                                      Boolean onlyUserLevel) {
        return Specification.allOf(
                hasRating(rating),
                hasDifficultyOrCategory(difficulty, category),
                isSolvedByUser(user.getId(), solvedFilter),
                hasTemplateId(templateId),
                filterByUserLevel(user.getLevel(), onlyUserLevel)
        );
    }
}