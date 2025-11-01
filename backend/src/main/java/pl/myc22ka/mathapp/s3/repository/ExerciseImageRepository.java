package pl.myc22ka.mathapp.s3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.s3.model.images.ExerciseImage;

import java.util.List;

/**
 * Repository interface for managing ExerciseImage entities.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
public interface ExerciseImageRepository extends JpaRepository<ExerciseImage, Long> {

    /**
     * Finds all exercise images associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of ExerciseImage entities
     */
    List<ExerciseImage> findByUserId(Long userId);
}
