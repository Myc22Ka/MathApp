package pl.myc22ka.mathapp.exercise.exercise.component.helper;

import org.springframework.stereotype.Component;

/**
 * Helper for validating template and variant inputs.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 13.09.2025
 */
@Component
public class ValidationHelper {

    /**
     * Validates that exactly one of templateId or variantId is provided.
     *
     * @param templateId the template ID
     * @param variantId the variant ID
     * @throws IllegalArgumentException if both or neither are provided
     */
    public void validateTemplateOrVariant(Long templateId, Long variantId) {
        if (templateId != null && variantId != null) {
            throw new IllegalArgumentException("Provide either templateId or variantId, not both");
        }
        if (templateId == null && variantId == null) {
            throw new IllegalArgumentException("Either templateId or variantId must be provided");
        }
    }

    public void validateFilters(Double rating, String difficulty) {
        if (rating != null && (rating < 0.0 || rating > 5.0)) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 5.0");
        }

        if (difficulty != null && difficulty.trim().isEmpty()) {
            throw new IllegalArgumentException("Difficulty cannot be empty string");
        }
    }
}
