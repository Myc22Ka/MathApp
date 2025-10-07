package pl.myc22ka.mathapp.ai.prompt.component.filter;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import pl.myc22ka.mathapp.ai.prompt.model.Modifier;
import pl.myc22ka.mathapp.model.expression.TemplatePrefix;

/**
 * Specifications for filtering Modifier entities.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @see Modifier
 * @since 01.10.2025
 */
public class ModifierSpecifications {

    /**
     * Filter by category (no-op if null).
     *
     * @param category the {@link TemplatePrefix} category of the modifier's topic
     * @return a {@link Specification} that restricts results by category,
     * or a "match all" specification if {@code category} is null
     */
    @NotNull
    public static Specification<Modifier> hasCategory(TemplatePrefix category) {
        return (root, query, cb) -> {
            if (category == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("topic").get("type"), category);
        };
    }

    /**
     * Build combined specification.
     *
     * @param category the {@link TemplatePrefix} category filter (nullable)
     * @return a {@link Specification} combining all defined filters
     */
    @NotNull
    public static Specification<Modifier> buildSpecification(TemplatePrefix category) {
        return Specification.allOf(
                hasCategory(category)
        );
    }
}
