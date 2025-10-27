package pl.myc22ka.mathapp.level.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.level.model.LevelRequirement;
import pl.myc22ka.mathapp.level.repository.LevelRequirementRepository;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelingService {

    private final LevelRequirementRepository levelRequirementRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addPointsAndCheckLevelUp(@NotNull User user, Double additionalPoints) {
        List<LevelRequirement> requirements = levelRequirementRepository.findAll();
        user.addPoints(additionalPoints, requirements);

        userRepository.save(user);
    }
}
