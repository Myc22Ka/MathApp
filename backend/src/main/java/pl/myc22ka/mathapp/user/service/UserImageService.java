package pl.myc22ka.mathapp.user.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
    private final ProfilePhotoRepository profilePhotoRepository;
    private final ExerciseImageRepository exerciseImageRepository;

    // ---------------- Upload ----------------
    @Transactional
    public void uploadProfilePhoto(User user, MultipartFile file) throws IOException {
        // usuń stare zdjęcia profilowe
        profilePhotoRepository.findAllByUser(user).forEach(photo -> {
            s3Service.deleteFile(photo.getUrl());
            profilePhotoRepository.delete(photo);
        });

        String url = s3Service.uploadFile(file);
        ProfilePhoto photo = ProfilePhoto.create(user, file, url);

        profilePhotoRepository.save(photo);
    }

    @Transactional
    public void uploadExerciseImage(User user, MultipartFile file, Long exerciseId) throws IOException {
        String url = s3Service.uploadFile(file);
        ExerciseImage img = ExerciseImage.create(user, file, url, exerciseId);

        exerciseImageRepository.save(img);
    }

    // ---------------- Listowanie ----------------
    public List<ExerciseImage> getExerciseImages(User user, Long exerciseId) {
        return exerciseImageRepository.findAllByUserAndRelatedExerciseId(user, exerciseId);
    }

    public String getProfilePhotoUrl(User user) {
        return profilePhotoRepository.findAllByUser(user)
                .stream()
                .findFirst()
                .map(ProfilePhoto::getUrl)
                .orElse(null);
    }

    // ---------------- Usuwanie ----------------
    @Transactional
    public void deleteImage(User user, Long id, @NotNull Class<? extends Image> type) {
        Image img;

        if (type.equals(ProfilePhoto.class)) {
            img = profilePhotoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Profile photo not found"));
        } else if (type.equals(ExerciseImage.class)) {
            img = exerciseImageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Exercise image not found"));
        } else {
            throw new RuntimeException("Unsupported image type");
        }

        if (!img.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Cannot delete someone else's image");
        }

        s3Service.deleteFile(img.getUrl());

        if (img instanceof ProfilePhoto) profilePhotoRepository.delete((ProfilePhoto) img);
        else exerciseImageRepository.delete((ExerciseImage) img);
    }
}
