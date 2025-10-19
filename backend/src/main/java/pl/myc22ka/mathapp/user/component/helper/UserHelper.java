package pl.myc22ka.mathapp.user.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exceptions.custom.UserException;
import pl.myc22ka.mathapp.user.model.Role;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;
import pl.myc22ka.mathapp.utils.security.dto.LoginRequest;
import pl.myc22ka.mathapp.utils.security.dto.RegisterRequest;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    public User buildUser(@NotNull RegisterRequest data) {
        return User.builder()
                .login(data.login())
                .email(data.email())
                .password(encoder.encode(data.password()))
                .role(Role.STUDENT)
                .points(0)
                .level(1)
                .dailyTasksCompleted(0)
                .lastDailyTaskDate(null)
                .build();
    }

    /**
     * Validates registration data before building a new User.
     *
     * @param data the registration data
     * @throws UserException if any validation rule fails
     */
    public void validateUserData(@NotNull RegisterRequest data) {
        if (data.email() == null || data.email().isBlank()) {
            throw new UserException("Email cannot be empty");
        }

        if (data.password() == null || data.password().isBlank()) {
            throw new UserException("Password cannot be empty");
        }

        if (userRepository.findByEmail(data.email()).isPresent()) {
            throw new UserException("A user with this email already exists");
        }

        if (data.login() != null && userRepository.findByLogin(data.login()).isPresent()) {
            throw new UserException("A user with this login already exists");
        }
    }

    /**
     * Authenticates a user by email and password.
     *
     * @param loginRequest the login credentials
     * @return the authenticated User
     * @throws UserException if credentials are invalid
     */
    public User authenticateUser(@NotNull LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UserException("Invalid credentials"));

        if (!encoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UserException("Invalid credentials");
        }

        return user;
    }
}
