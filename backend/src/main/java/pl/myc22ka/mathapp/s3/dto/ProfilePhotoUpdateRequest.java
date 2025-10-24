package pl.myc22ka.mathapp.s3.dto;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ProfilePhotoUpdateRequest(
        @NotNull MultipartFile file
) {}
