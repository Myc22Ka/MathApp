package pl.myc22ka.mathapp.s3.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.exceptions.DefaultResponse;
import pl.myc22ka.mathapp.s3.model.Image;
import pl.myc22ka.mathapp.s3.model.ImageType;
import pl.myc22ka.mathapp.s3.model.images.ExerciseImage;
import pl.myc22ka.mathapp.s3.model.images.ProfilePhoto;
import pl.myc22ka.mathapp.s3.service.S3Service;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.service.UserImageService;
import pl.myc22ka.mathapp.utils.security.service.AuthService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final UserImageService imageService;

    // ---------------- Upload ----------------
    @PostMapping(name = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DefaultResponse> uploadImage(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            @NotNull @RequestParam("type") ImageType type,
            @RequestParam(value = "exerciseId", required = false) Long exerciseId
    ) throws IOException {

        switch (type) {
            case PROFILE -> imageService.uploadProfilePhoto(user, file);
            case EXERCISE -> {
                if (exerciseId == null) {
                    return ResponseEntity.badRequest()
                            .body(new DefaultResponse(LocalDate.now().toString(), "exerciseId is required for EXERCISE image", 400));
                }
                imageService.uploadExerciseImage(user, file, exerciseId);
            }
        }

        return ResponseEntity.ok(new DefaultResponse(LocalDate.now().toString(), "Image uploaded successfully", 200));
    }

    // ---------------- Listowanie ----------------
    @GetMapping("/exercise/{exerciseId}")
    public List<ExerciseImage> listExerciseImages(
            @AuthenticationPrincipal User user,
            @PathVariable Long exerciseId
    ) {
        return imageService.getExerciseImages(user, exerciseId);
    }

    // ---------------- Usuwanie ----------------
    @DeleteMapping("/delete")
    public ResponseEntity<DefaultResponse> deleteImage(
            @AuthenticationPrincipal User user,
            @NotNull @RequestParam("type") ImageType type,
            @RequestParam("id") Long id
    ) {
        Class<? extends Image> clazz = switch (type) {
            case PROFILE -> ProfilePhoto.class;
            case EXERCISE -> ExerciseImage.class;
        };

        imageService.deleteImage(user, id, clazz);
        return ResponseEntity.ok(new DefaultResponse(LocalDate.now().toString(), "Image deleted successfully", 200));
    }
}