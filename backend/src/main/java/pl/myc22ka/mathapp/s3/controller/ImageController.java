package pl.myc22ka.mathapp.s3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * REST controller for managing user images, including upload, download,
 * listing, and deletion of profile and exercise images.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Images", description = "APIs for managing user images")
public class ImageController {

    private final UserImageService imageService;
    private final AuthHelper authHelper;
    private final ImageHelper imageHelper;

    /**
     * Uploads an image for the authenticated user.
     *
     * @param user       the authenticated user
     * @param file       the image file to upload
     * @param type       the type of image (PROFILE or EXERCISE)
     * @param exerciseId the ID of the exercise (required if type is EXERCISE)
     * @return a response indicating success or failure
     * @throws IOException if an I/O error occurs during upload
     */
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

    /**
     * Downloads an image for the authenticated user.
     *
     * @param user the authenticated user
     * @param type the type of image (PROFILE or EXERCISE)
     * @param id   the ID of the image to download (optional for PROFILE)
     * @return the image file as a byte array with appropriate headers
     */
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

    /**
     * Retrieves all exercise images for the authenticated user.
     *
     * @param user       the authenticated user
     * @param exerciseId the ID of the exercise
     * @return a list of image responses
     */
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

    /**
     * Deletes an image for the authenticated user.
     *
     * @param user the authenticated user
     * @param type the type of image (PROFILE or EXERCISE)
     * @param id   the ID of the image to delete (optional for PROFILE)
     * @return a response indicating success or failure
     */
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