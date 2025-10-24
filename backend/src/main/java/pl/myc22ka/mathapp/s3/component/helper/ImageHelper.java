package pl.myc22ka.mathapp.s3.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exceptions.custom.ResourceNotFoundException;
import pl.myc22ka.mathapp.s3.model.Image;
import pl.myc22ka.mathapp.s3.model.images.ProfilePhoto;
import pl.myc22ka.mathapp.s3.repository.ExerciseImageRepository;
import pl.myc22ka.mathapp.s3.repository.ProfilePhotoRepository;
import pl.myc22ka.mathapp.user.model.User;

@Component
@RequiredArgsConstructor
public class ImageHelper {

    private final ProfilePhotoRepository profilePhotoRepository;
    private final ExerciseImageRepository exerciseImageRepository;

    public Image getImageInfo(User user, Long id, @NotNull Class<? extends Image> type) {
        if (type.equals(ProfilePhoto.class)) {
            return profilePhotoRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile photo not found"));
        } else {
            return exerciseImageRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise image not found"));
        }
    }
}
