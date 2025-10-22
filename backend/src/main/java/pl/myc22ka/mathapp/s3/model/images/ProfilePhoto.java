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
@Table(name = "profile_photos")
@Data
@NoArgsConstructor
public class ProfilePhoto extends Image {

    @NotNull
    public static ProfilePhoto create(User user, MultipartFile file, String url) throws IOException {
        ProfilePhoto photo = new ProfilePhoto();
        photo.setData(user, file, url);
        return photo;
    }
}
