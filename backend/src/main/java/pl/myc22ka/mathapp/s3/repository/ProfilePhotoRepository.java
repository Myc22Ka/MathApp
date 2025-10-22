package pl.myc22ka.mathapp.s3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.s3.model.images.ProfilePhoto;
import pl.myc22ka.mathapp.user.model.User;

import java.util.List;

public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Long> {
    List<ProfilePhoto> findAllByUser(User user);
}
