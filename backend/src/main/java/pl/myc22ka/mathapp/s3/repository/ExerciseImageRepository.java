package pl.myc22ka.mathapp.s3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.s3.model.images.ExerciseImage;
import pl.myc22ka.mathapp.user.model.User;

import java.util.List;

public interface ExerciseImageRepository extends JpaRepository<ExerciseImage, Long> {
    List<ExerciseImage> findAllByUserAndRelatedExerciseId(User user, Long exerciseId);
}
