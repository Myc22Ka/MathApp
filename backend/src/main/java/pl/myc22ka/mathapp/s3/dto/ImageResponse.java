package pl.myc22ka.mathapp.s3.dto;

import pl.myc22ka.mathapp.s3.model.images.ExerciseImage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Data Transfer Object (DTO) for transferring image data related to exercises.
 *
 * @param id                 the unique identifier of the image
 * @param url                the URL where the image is stored
 * @param relatedExerciseId  the ID of the exercise related to the image
 * @param uploadedAt         the timestamp when the image was uploaded
 */
public record ImageResponse(
        Long id,
        String url,
        Long relatedExerciseId,
        LocalDateTime uploadedAt
) {
    /**
     * Converts a list of ExerciseImage entities to a list of ImageResponse DTOs.
     *
     * @param exerciseImages the list of ExerciseImage entities
     * @return a list of ImageResponse DTOs
     */
    public static List<ImageResponse> fromEntity(List<ExerciseImage> exerciseImages) {
        if (exerciseImages == null) {
            return Collections.emptyList();
        }

        return exerciseImages.stream()
                .map(img -> new ImageResponse(
                        img.getId(),
                        img.getUrl(),
                        img.getRelatedExerciseId(),
                        img.getUploadedAt()
                ))
                .toList();
    }
}
