package pl.myc22ka.mathapp.utils.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.exceptions.custom.UserException;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;
import pl.myc22ka.mathapp.user.service.UserImageService;
import pl.myc22ka.mathapp.utils.microservices.mail.service.EmailService;
import pl.myc22ka.mathapp.utils.security.component.helper.AuthHelper;
import pl.myc22ka.mathapp.utils.security.dto.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthHelper authHelper;
    private final UserHelper userHelper;
    private final UserImageService userImageService;
    private final EmailService emailService;
    private final PasswordEncoder encoder;

    @Transactional
    public User register(RegisterRequest userData) {
        authHelper.validateUserData(userData);
        User user = authHelper.buildUser(userData);

        userRepository.save(user);

        String verificationCode = authHelper.generateVerificationCode(user);

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


    @Transactional
    public User login(LoginRequest loginRequest) {
        User user = authHelper.authenticateUser(loginRequest);

        // Jeśli 2FA jest wyłączone, zwracamy użytkownika
        if (!user.getTwoFactorEnabled()) {
            return user;
        }

        // 2FA jest włączone - generujemy kod i wysyłamy email
        String verificationCode = authHelper.generateVerificationCode(user);

        emailService.sendEmailFromTemplate(
                user.getEmail(),
                "Verification Code",
                "verification_code.html",
                Map.of(
                        "name", user.getLogin(),
                        "verificationCode", verificationCode
                )
        );

        return user;
    }

    @Transactional
    public void deleteUser(@NotNull User user, @NotNull DeleteAccountRequest deleteAccountRequest) {

        if (!user.matchesPassword(deleteAccountRequest.password(), encoder)) return;

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

    public void resendCode(String email) {
        User user = userHelper.getUserByEmail(email);

        String verificationCode = authHelper.generateVerificationCode(user);

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
    }

    public void verifyCode(User user, String code) {
        authHelper.verifyCode(user, code);
    }

    @Transactional
    public void requestPasswordChange(@NotNull String email) {
        User user = userHelper.getUserByEmail(email);

        String resetToken = authHelper.generatePasswordResetToken(user);
        String verificationLink = emailService.generateVerificationLink("forgot-password/request") + "?token=" + resetToken;

        userRepository.save(user);

        emailService.sendEmailFromTemplate(
                user.getEmail(),
                "MathApp Password Reset Code",
                "password_reset.html",
                Map.of(
                        "name", user.getLogin(),
                        "verificationLink", verificationLink
                )
        );
    }

    @Transactional
    public void confirmPasswordChange(@NotNull String code, @NotNull String newPassword) {
        User user = userHelper.getUserByCode(code);

        authHelper.validatePassword(newPassword);

        user.setPassword(authHelper.encodePassword(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void changePasswordLoggedIn(@NotNull User user, String oldPassword, String newPassword) {
        if (!user.matchesPassword(oldPassword, encoder)) {
            throw new UserException("Incorrect current password");
        }

        authHelper.validatePassword(newPassword);
        user.setPassword(authHelper.encodePassword(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public User updateProfile(User user, @NotNull UpdateProfileRequest request) {
        if (request.firstname() != null) user.setFirstname(request.firstname());
        if (request.lastname() != null) user.setLastname(request.lastname());
        if (request.phoneNumber() != null) user.setPhoneNumber(request.phoneNumber());
        if (request.address() != null) user.setAddress(request.address());
        if (request.dateOfBirth() != null) user.setDateOfBirth(request.dateOfBirth());
        if (request.gender() != null) user.setGender(request.gender());
        if (request.notificationsEnabled() != null) user.setNotificationsEnabled(request.notificationsEnabled());
        if(request.twoFactorAuthEnabled() != null) user.setTwoFactorEnabled(request.twoFactorAuthEnabled());

        return userRepository.save(user);
    }
}

