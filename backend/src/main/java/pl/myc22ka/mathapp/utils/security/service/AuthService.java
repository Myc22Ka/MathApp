package pl.myc22ka.mathapp.utils.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;
import pl.myc22ka.mathapp.utils.security.dto.LoginRequest;
import pl.myc22ka.mathapp.utils.security.dto.RegisterRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserHelper userHelper;

    @Transactional
    public User register(RegisterRequest userData) {
        userHelper.validateUserData(userData);

        User user = userHelper.buildUser(userData);
        userRepository.save(user);

        return user;
    }

    public User login(LoginRequest loginRequest) {
        return userHelper.authenticateUser(loginRequest);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}

