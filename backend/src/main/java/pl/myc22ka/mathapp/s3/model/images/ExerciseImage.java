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
import pl.myc22ka.mathapp.user.model.User;

import java.io.IOException;

@Entity
@Table(name = "exercises_images")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExerciseImage extends Image {

    @Column(nullable = false)
    private Long relatedExerciseId;

    @NotNull
    public static ExerciseImage create(Long userId, MultipartFile file, String url, String s3Key, Long relatedExerciseId) throws IOException {
        ExerciseImage img = new ExerciseImage();
        img.setData(userId, file, url, s3Key);
        img.setRelatedExerciseId(relatedExerciseId);

        return img;
    }
}