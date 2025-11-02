package pl.myc22ka.mathapp.s3.dto;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO for profile photo update requests.
 *
 * @param file the new profile photo file
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
public record ProfilePhotoUpdateRequest(
        @NotNull MultipartFile file
) {}
