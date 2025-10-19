package pl.myc22ka.mathapp.utils.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.utils.security.component.helper.AuthHelper;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;
import pl.myc22ka.mathapp.utils.security.dto.LoginRequest;
import pl.myc22ka.mathapp.utils.security.dto.RegisterRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthHelper authHelper;
    private final UserHelper userHelper;

    @Transactional
    public User register(RegisterRequest userData) {
        authHelper.validateUserData(userData);

        User user = authHelper.buildUser(userData);
        userRepository.save(user);

        return user;
    }

    public User login(LoginRequest loginRequest) {
        return authHelper.authenticateUser(loginRequest);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public String generateCode(String email) {
        User user = userHelper.getUserByEmail(email);

        return authHelper.generateVerificationCode(user);
    }

    public void verifyCode(String email, String code) {
        User user = userHelper.getUserByEmail(email);

        authHelper.verifyCode(user, code);
    }
}

