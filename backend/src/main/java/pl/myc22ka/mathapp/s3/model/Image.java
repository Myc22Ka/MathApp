package pl.myc22ka.mathapp.s3.model;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base class for all images uploaded by users.
 * Contains common metadata such as file name, S3 key, URL, content type, size, and upload time.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String fileName;
    private String s3Key;
    private String url;

    private String contentType;

    private Long size;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    public void setData(Long userId, @NotNull MultipartFile file, String url, String s3Key) throws IOException {
        this.userId = userId;
        this.fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        this.url = url;
        this.s3Key = s3Key;
        this.contentType = file.getContentType();
        this.size = file.getSize();
        this.uploadedAt = LocalDateTime.now();
    }
}