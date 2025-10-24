package pl.myc22ka.mathapp.s3.dto;

import pl.myc22ka.mathapp.s3.model.images.ExerciseImage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record ImageResponse(
        Long id,
        String url,
        Long relatedExerciseId,
        LocalDateTime uploadedAt
) {
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
