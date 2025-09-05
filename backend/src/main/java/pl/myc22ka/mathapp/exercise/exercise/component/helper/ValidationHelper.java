package pl.myc22ka.mathapp.exercise.exercise.component.helper;

import org.springframework.stereotype.Component;

@Component
public class ValidationHelper {

    public void validateTemplateOrVariant(Long templateId, Long variantId) {
        if (templateId != null && variantId != null) {
            throw new IllegalArgumentException("Provide either templateId or variantId, not both");
        }
        if (templateId == null && variantId == null) {
            throw new IllegalArgumentException("Either templateId or variantId must be provided");
        }
    }
}
