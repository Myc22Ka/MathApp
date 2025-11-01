package pl.myc22ka.mathapp.s3.model.images;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.s3.model.Image;

import java.io.IOException;

/**
 * Entity representing an image associated with an exercise.
 *
 * @author Myc22ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Entity
@Table(name = "exercises_images")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExerciseImage extends Image {

    @Column(nullable = false)
    private Long relatedExerciseId;

    /**
     * Factory method to create an ExerciseImage instance.
     *
     * @param userId            ID of the user uploading the image
     * @param file              MultipartFile representing the image
     * @param url               URL where the image is stored
     * @param s3Key             S3 key for the image
     * @param relatedExerciseId ID of the related exercise
     * @return A new ExerciseImage instance
     * @throws IOException If an I/O error occurs during file processing
     */
    @NotNull
    public static ExerciseImage create(Long userId, MultipartFile file, String url, String s3Key, Long relatedExerciseId) throws IOException {
        ExerciseImage img = new ExerciseImage();
        img.setData(userId, file, url, s3Key);
        img.setRelatedExerciseId(relatedExerciseId);

        return img;
    }
}