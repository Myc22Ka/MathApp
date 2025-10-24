package pl.myc22ka.mathapp.s3.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.exceptions.DefaultResponse;
import pl.myc22ka.mathapp.s3.component.helper.ImageHelper;
import pl.myc22ka.mathapp.s3.dto.ImageResponse;
import pl.myc22ka.mathapp.s3.model.Image;
import pl.myc22ka.mathapp.s3.model.ImageType;
import pl.myc22ka.mathapp.s3.model.images.ExerciseImage;
import pl.myc22ka.mathapp.s3.model.images.ProfilePhoto;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.service.UserImageService;
import pl.myc22ka.mathapp.utils.security.component.helper.AuthHelper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final UserImageService imageService;
    private final AuthHelper authHelper;
    private final ImageHelper imageHelper;

    // ---------------- Upload ----------------
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload an image")
    public ResponseEntity<DefaultResponse> uploadImage(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            @NotNull @RequestParam("type") ImageType type,
            @RequestParam(value = "exerciseId", required = false) Long exerciseId
    ) throws IOException {

        authHelper.validateUser(user);

        switch (type) {
            case PROFILE -> imageService.uploadProfilePhoto(user, file);
            case EXERCISE -> {
                if (exerciseId == null) {
                    return ResponseEntity.badRequest()
                            .body(new DefaultResponse(
                                    LocalDateTime.now().toString(),
                                    "exerciseId is required for EXERCISE image",
                                    400
                            ));
                }
                imageService.uploadExerciseImage(user, file, exerciseId);
            }
        }

        return ResponseEntity.ok(new DefaultResponse(
                LocalDateTime.now().toString(),
                "Image uploaded successfully",
                200
        ));
    }

    // ---------------- Download ----------------
    @GetMapping("/download")
    @Operation(summary = "Download an image")
    public ResponseEntity<byte[]> downloadImage(
            @AuthenticationPrincipal User user,
            @NotNull @RequestParam("type") ImageType type,
            @RequestParam(value = "id", required = false) Long id
    ) {
        authHelper.validateUser(user);

        Class<? extends Image> clazz = switch (type) {
            case PROFILE -> ProfilePhoto.class;
            case EXERCISE -> ExerciseImage.class;
        };

        byte[] data = imageService.downloadImage(user, id, clazz);

        // Pobranie informacji o pliku (nazwa, typ)
        Image img = imageHelper.getImageInfo(user, id, clazz);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(img.getContentType()));
        headers.setContentLength(data.length);
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename(img.getFileName())
                        .build()
        );

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    // ---------------- Listowanie ----------------
    @GetMapping("/exercise/{exerciseId}")
    @Operation(summary = "Get all images for an exercise")
    public ResponseEntity<List<ImageResponse>> getExerciseImages(
            @AuthenticationPrincipal User user,
            @PathVariable Long exerciseId
    ) {
        authHelper.validateUser(user);
        List<ImageResponse> images = imageService.getExerciseImages(user.getId());
        return ResponseEntity.ok(images);
    }

    // ---------------- Usuwanie ----------------
    @DeleteMapping("/delete")
    @Operation(summary = "Delete an image")
    public ResponseEntity<DefaultResponse> deleteImage(
            @AuthenticationPrincipal User user,
            @NotNull @RequestParam("type") ImageType type,
            @RequestParam(value = "id", required = false) Long id
    ) {
        authHelper.validateUser(user);

        Class<? extends Image> clazz = switch (type) {
            case PROFILE -> ProfilePhoto.class;
            case EXERCISE -> ExerciseImage.class;
        };

        imageService.deleteImage(user, id, clazz);
        return ResponseEntity.ok(new DefaultResponse(
                LocalDateTime.now().toString(),
                "Image deleted successfully",
                200
        ));
    }
}