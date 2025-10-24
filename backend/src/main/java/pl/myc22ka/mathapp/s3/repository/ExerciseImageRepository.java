package pl.myc22ka.mathapp.s3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.s3.model.images.ExerciseImage;

import java.util.List;

public interface ExerciseImageRepository extends JpaRepository<ExerciseImage, Long> {

    List<ExerciseImage> findByUserId(Long userId);
}
