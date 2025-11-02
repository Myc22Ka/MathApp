package pl.myc22ka.mathapp.utils.security.component.helper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exceptions.custom.UserException;
import pl.myc22ka.mathapp.user.model.Role;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;
import pl.myc22ka.mathapp.utils.security.dto.LoginRequest;
import pl.myc22ka.mathapp.utils.security.dto.RegisterRequest;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Helper class for authentication-related operations.
 * Provides methods for building and validating users, password encoding,
 * generating and verifying verification codes, and authenticating users.
 * <p>
 * Used throughout the application for registration, login, and password verification.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.3
 * @since 01.11.2025
 */
@Component
@RequiredArgsConstructor
public class AuthHelper {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final Random random = new Random();

    /**
     * Builds a new User entity from registration data.
     *
     * @param data the registration data
     * @return a new User entity
     */
    public User buildUser(@NotNull RegisterRequest data) {
        return User.builder()
                .login(data.login())
                .email(data.email())
                .password(encoder.encode(data.password()))
                .role(Role.STUDENT)
                .points(0.0)
                .level(1)
                .dailyTasksCompleted(0)
                .lastDailyTaskDate(null)
                .verified(false)
                .twoFactorEnabled(false)
                .notificationsEnabled(true)
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
     * Validates that a user is authenticated.
     *
     * @param user the user object from Spring Security context
     * @throws AccessDeniedException if the user is not authenticated
     */
    public void validateUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new AccessDeniedException("User not authenticated");
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

    /**
     * Generates a 6-digit verification code for the user and sets an expiration time.
     *
     * @param user the user to generate the code for
     * @return the generated verification code
     */
    public String generateVerificationCode(@NotNull User user) {
        String code = String.valueOf(random.nextInt(900_000) + 100_000);
        user.setVerificationCode(code);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setVerified(false);

        userRepository.save(user);
        return code;
    }

    /**
     * Verifies the code provided by the user.
     *
     * @param user the user to verify
     * @param code the verification code provided by the user
     * @throws UserException if the code is invalid, expired, or the account is already verified
     */
    public void verifyCode(@NotNull User user, @NotNull String code) {
        if (user.isVerified()) {
            throw new UserException("Account is already verified");
        }

        if (user.getVerificationCodeExpiresAt() == null || user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UserException("Verification code has expired");
        }

        if (!code.equals(user.getVerificationCode())) {
            throw new UserException("Invalid verification code");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);

        userRepository.save(user);
    }

    /**
     * Validates password complexity.
     *
     * @param password the password to validate
     * @throws IllegalArgumentException if password is null, empty, or too short
     */
    public void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    /**
     * Checks whether a raw password matches an encoded password.
     *
     * @param rawPassword     the raw password
     * @param encodedPassword the encoded password
     * @return true if matches, false otherwise
     */
    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Encodes a raw password using BCrypt.
     *
     * @param rawPassword the password to encode
     * @return the encoded password
     */
    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
