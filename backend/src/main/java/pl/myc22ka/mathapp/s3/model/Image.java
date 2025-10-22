package pl.myc22ka.mathapp.s3.model;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String url;

    private String contentType;

    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    public void setData(User user, @NotNull MultipartFile file, String url) throws IOException {
        this.user = user;
        this.fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        this.url = url;
        this.contentType = file.getContentType();
        this.size = file.getSize();
        this.uploadedAt = LocalDateTime.now();
    }
}
