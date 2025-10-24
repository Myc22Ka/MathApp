package pl.myc22ka.mathapp.s3.model.images;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.s3.model.Image;

import java.io.IOException;

@Entity
@Table(name = "profile_photos")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfilePhoto extends Image {

    @NotNull
    public static ProfilePhoto create(Long userId, MultipartFile file, String url, String s3Key) throws IOException {
        ProfilePhoto photo = new ProfilePhoto();
        photo.setData(userId, file, url, s3Key);

        return photo;
    }
}
