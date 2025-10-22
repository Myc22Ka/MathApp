package pl.myc22ka.mathapp.s3.model.images;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.s3.model.Image;
import pl.myc22ka.mathapp.user.model.User;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "exercises_images")
@Data
@NoArgsConstructor
public class ExerciseImage extends Image {

    private Long relatedExerciseId;

    @NotNull
    public static ExerciseImage create(User user, MultipartFile file, String url, Long relatedExerciseId) throws IOException {
        ExerciseImage img = new ExerciseImage();
        img.setData(user, file, url);
        img.setRelatedExerciseId(relatedExerciseId);
        return img;
    }
}