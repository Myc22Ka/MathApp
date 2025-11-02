package pl.myc22ka.mathapp.utils.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;
import pl.myc22ka.mathapp.user.service.UserImageService;
import pl.myc22ka.mathapp.utils.microservices.mail.service.EmailService;
import pl.myc22ka.mathapp.utils.security.component.helper.AuthHelper;
import pl.myc22ka.mathapp.utils.security.dto.LoginRequest;
import pl.myc22ka.mathapp.utils.security.dto.RegisterRequest;
import pl.myc22ka.mathapp.utils.security.dto.TwoFactorRequest;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthHelper authHelper;
    private final UserHelper userHelper;
    private final UserImageService userImageService;
    private final EmailService emailService;

    @Transactional
    public User register(RegisterRequest userData) {
        authHelper.validateUserData(userData);
        User user = authHelper.buildUser(userData);

        userRepository.save(user);

        String verificationCode = generateCode(user.getEmail());

        userRepository.save(user);

        String verificationLink = emailService.generateVerificationLink("verify") + "?code=" + verificationCode;
        emailService.sendEmailFromTemplate(
                user.getEmail(),
                "Account Verification",
                "welcome.html",
                Map.of("name", user.getLogin(),
                        "verificationCode", verificationCode,
                        "verificationLink", verificationLink)
        );

        return user;
    }

    public User login(LoginRequest loginRequest) {
        return authHelper.authenticateUser(loginRequest);
    }

    @Transactional
    public void deleteUser(User user) {
        userImageService.deleteAllUserImages(user);

        userRepository.delete(user);

        emailService.sendEmailFromTemplate(
                user.getEmail(),
                "Your MathApp Account Has Been Deleted",
                "account_deleted.html",
                Map.of(
                        "name", user.getLogin(),
                        "signupLink", emailService.generateVerificationLink("register")
                )
        );
    }

    public String generateCode(String email) {
        User user = userHelper.getUserByEmail(email);

        return authHelper.generateVerificationCode(user);
    }

    public void verifyCode(String email, String code) {
        User user = userHelper.getUserByEmail(email);

        authHelper.verifyCode(user, code);
    }

    @Transactional
    public void requestPasswordChange(@NotNull String email) {
        User user = userHelper.getUserByEmail(email);

        String verificationCode = generateCode(user.getEmail());
        userRepository.save(user);

        emailService.sendEmailFromTemplate(
                user.getEmail(),
                "MathApp Password Reset Code",
                "password_reset.html",
                Map.of(
                        "name", user.getLogin(),
                        "verificationCode", verificationCode
                )
        );
    }

    @Transactional
    public void confirmPasswordChange(@NotNull String email, @NotNull String code, @NotNull String newPassword) {
        User user = userHelper.getUserByEmail(email);

        authHelper.verifyCode(user, code);
        authHelper.validatePassword(newPassword);

        user.setPassword(authHelper.encodePassword(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void changePasswordLoggedIn(@NotNull User user, String oldPassword, String newPassword) {
        if (!authHelper.passwordMatches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password");
        }

        authHelper.validatePassword(newPassword);
        user.setPassword(authHelper.encodePassword(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updateTwoFactorStatus(@NotNull User user, @NotNull TwoFactorRequest request) {
        user.setTwoFactorEnabled(request.enabled());

        userRepository.save(user);
    }
}

