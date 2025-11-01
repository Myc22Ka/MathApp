package pl.myc22ka.mathapp.s3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.s3.model.images.ProfilePhoto;

import java.util.Optional;

/**
 * Repository interface for managing ProfilePhoto entities.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Long> {

    /**
     * Finds a ProfilePhoto by the associated user ID.
     *
     * @param userId the ID of the user
     * @return an Optional containing the ProfilePhoto if found, or empty if not found
     */
    Optional<ProfilePhoto> findByUserId(Long userId);
}
