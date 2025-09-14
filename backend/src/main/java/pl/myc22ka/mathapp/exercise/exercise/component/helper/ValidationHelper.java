package pl.myc22ka.mathapp.exercise.exercise.component.helper;

import org.springframework.stereotype.Component;

/**
 * Helper for validating template and variant inputs.
 *
 * @author Myc22Ka
 * @version 1.0.0
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
}
