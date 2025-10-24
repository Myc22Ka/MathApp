package pl.myc22ka.mathapp.user.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.myc22ka.mathapp.exceptions.custom.ResourceNotFoundException;
import pl.myc22ka.mathapp.s3.component.helper.S3Helper;
import pl.myc22ka.mathapp.s3.dto.ImageResponse;
import pl.myc22ka.mathapp.s3.model.Image;
import pl.myc22ka.mathapp.s3.model.images.ExerciseImage;
import pl.myc22ka.mathapp.s3.model.images.ProfilePhoto;
import pl.myc22ka.mathapp.s3.repository.ExerciseImageRepository;
import pl.myc22ka.mathapp.s3.repository.ProfilePhotoRepository;
import pl.myc22ka.mathapp.s3.service.S3Service;
import pl.myc22ka.mathapp.user.model.User;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserImageService {

    private final S3Service s3Service;
    private final S3Helper s3Helper;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final ExerciseImageRepository exerciseImageRepository;

    // ---------------- Upload ----------------
    @Transactional
    public void uploadProfilePhoto(User user, @NotNull MultipartFile file) throws IOException {
        profilePhotoRepository.findByUserId(user.getId()).ifPresent(old -> {
            s3Service.deleteFile(old.getS3Key());
            profilePhotoRepository.delete(old);
        });

        String s3Key = s3Helper.generateS3Key(file.getOriginalFilename());
        String url = s3Service.uploadFile(file, s3Key);

        ProfilePhoto img = ProfilePhoto.create(user.getId(), file, url, s3Key);
        profilePhotoRepository.save(img);
    }

    @Transactional
    public void uploadExerciseImage(User user, @NotNull MultipartFile file, Long exerciseId) throws IOException {
        String s3Key = s3Helper.generateS3Key(file.getOriginalFilename());
        String url = s3Service.uploadFile(file, s3Key);

        ExerciseImage img = ExerciseImage.create(user.getId(), file, url, s3Key, exerciseId);
        exerciseImageRepository.save(img);
    }

    // ---------------- Download ----------------
    @Transactional(readOnly = true)
    public byte[] downloadImage(User user, Long imageId, @NotNull Class<? extends Image> type) {
        Image img;

        if (type.equals(ProfilePhoto.class)) {
            img = profilePhotoRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile photo not found"));
        } else if (type.equals(ExerciseImage.class)) {
            img = exerciseImageRepository.findById(imageId)
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise image not found"));
        } else {
            throw new IllegalArgumentException("Unsupported image type");
        }

        if (!img.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("Cannot access someone else's image");
        }

        return s3Service.downloadFile(img.getS3Key());
    }

    // ---------------- Listowanie ----------------
    @Transactional(readOnly = true)
    public List<ImageResponse> getExerciseImages(Long userId) {
        List<ExerciseImage> images = exerciseImageRepository.findByUserId(userId);
        return ImageResponse.fromEntity(images);
    }

    @Transactional(readOnly = true)
    public String getProfilePhotoUrl(User user) {
        return profilePhotoRepository.findByUserId(user.getId())
                .map(ProfilePhoto::getUrl)
                .orElse(null);
    }

    // ---------------- Usuwanie ----------------
    @Transactional
    public void deleteImage(User user, Long id, @NotNull Class<? extends Image> type) {
        Image img;

        if (type.equals(ProfilePhoto.class)) {
            if (id == null) {
                img = profilePhotoRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Profile photo not found"));
            } else {
                img = profilePhotoRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Profile photo not found"));
            }
        } else if (type.equals(ExerciseImage.class)) {
            if (id == null) {
                throw new IllegalArgumentException("Exercise image ID is required");
            }
            img = exerciseImageRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise image not found"));
        } else {
            throw new IllegalArgumentException("Unsupported image type");
        }

        if (!img.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("Cannot delete someone else's image");
        }

        s3Service.deleteFile(img.getS3Key());

        if (img instanceof ProfilePhoto) {
            profilePhotoRepository.delete((ProfilePhoto) img);
        } else {
            exerciseImageRepository.delete((ExerciseImage) img);
        }
    }
}