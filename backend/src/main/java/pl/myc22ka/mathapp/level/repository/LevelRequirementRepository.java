package pl.myc22ka.mathapp.level.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.myc22ka.mathapp.level.model.LevelRequirement;

import java.util.List;
import java.util.Optional;

public interface LevelRequirementRepository extends JpaRepository<LevelRequirement, Long> {
    Optional<LevelRequirement> findByLevel(int level);
    List<LevelRequirement> findAllByOrderByLevelAsc();
}