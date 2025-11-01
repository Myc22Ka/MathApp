package pl.myc22ka.mathapp.s3.model.images;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.s3.model.Image;

import java.io.IOException;

/**
 * Entity representing a user's profile photo.
 * Inherits common image metadata from the Image base class.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Entity
@Table(name = "profile_photos")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfilePhoto extends Image {

    /**
     * Factory method to create a ProfilePhoto instance from user ID and file data.
     *
     * @param userId the ID of the user
     * @param file   the MultipartFile representing the photo
     * @param url    the URL of the uploaded photo in S3
     * @param s3Key  the S3 key of the uploaded photo
     * @return a new ProfilePhoto instance
     * @throws IOException if an I/O error occurs during file processing
     */
    @NotNull
    public static ProfilePhoto create(Long userId, MultipartFile file, String url, String s3Key) throws IOException {
        ProfilePhoto photo = new ProfilePhoto();
        photo.setData(userId, file, url, s3Key);

        return photo;
    }
}
