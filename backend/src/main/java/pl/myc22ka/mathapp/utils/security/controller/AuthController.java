package pl.myc22ka.mathapp.utils.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exceptions.DefaultResponse;
import pl.myc22ka.mathapp.s3.dto.ImageResponse;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.user.dto.UserDTO;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.service.UserImageService;
import pl.myc22ka.mathapp.utils.security.component.helper.CookieHelper;
import pl.myc22ka.mathapp.utils.security.dto.*;
import pl.myc22ka.mathapp.utils.security.service.AuthService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST controller for authentication and user management.
 * Provides endpoints for user registration, login, logout,
 * password management, and two-factor authentication.
 *
 * @author Myc22Ka
 * @version 1.0.3
 * @since 01.11.2025
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations for authentication and user management")
public class AuthController {

    private final AuthService authService;
    private final UserImageService imageService;
    private final CookieHelper cookieHelper;
    private final UserHelper userHelper;

    @Value("${spring.scheduler.daily-exercise-cron}")
    private String cronExpression;

    @NotNull
    private UserDTO buildUserDTO(User user, HttpServletResponse response) {
        String profilePhotoUrl = imageService.getProfilePhotoUrl(user);
        List<ImageResponse> exerciseImages = imageService.getExerciseImages(user.getId());
        return UserDTO.fromEntity(user, profilePhotoUrl, exerciseImages, cronExpression);
    }

    /**
     * Registers a new user, generates a JWT token, sets it in an HttpOnly cookie,
     * and returns the created user data.
     *
     * @param registerRequest user registration data
     * @param response        HTTP response (used to set cookie)
     * @return registered user data with profile photo URL
     */
    @Operation(
            summary = "Register new user",
            description = "Registers a new user, creates their account, sends a verification email, and sets an authentication cookie."
    )
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @RequestBody RegisterRequest registerRequest,
            @NotNull HttpServletResponse response
    ) {
        User user = authService.register(registerRequest);
        cookieHelper.setAuthCookie(user, response);

        return ResponseEntity.ok(
                UserDTO.fromEntity(
                        user,
                        null,
                        null,
                        cronExpression
                )
        );
    }

    /**
     * Authenticates a user and sets a JWT cookie.
     *
     * @param loginRequest login credentials
     * @param response     HTTP response (used to set cookie)
     * @return user data including profile and exercise images
     */
    @Operation(
            summary = "User login",
            description = "Authenticates a user with email and password, sets an authentication cookie, and returns user details."
    )
    @PostMapping("/sign-in")
    public ResponseEntity<UserDTO> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        User user = authService.login(loginRequest);

        if (user.getTwoFactorEnabled()) {
            return ResponseEntity.ok(UserDTO.forTwoFactorAuth(user));
        }

        cookieHelper.setAuthCookie(user, response);

        return ResponseEntity.ok(buildUserDTO(user, response));
    }

    /**
     * Returns data about the currently authenticated user.
     *
     * @param user authenticated user
     * @return user data including profile and exercise images
     */
    @Operation(
            summary = "Get current user info",
            description = "Returns the currently authenticated user's data, including profile and exercise images."
    )
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(
            @AuthenticationPrincipal User user,
            @NotNull HttpServletResponse response
    ) {
        if (user == null) {
            cookieHelper.clearAuthCookie(response);
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(buildUserDTO(user, response));
    }

    /**
     * Logs out the current user by clearing their authentication cookie.
     *
     * @param response HTTP response to clear cookie
     * @return success message
     */
    @Operation(
            summary = "User logout",
            description = "Clears the authentication cookie, effectively logging out the current user."
    )
    @PostMapping("/sign-out")
    public ResponseEntity<DefaultResponse> logout(@NotNull HttpServletResponse response) {
        cookieHelper.clearAuthCookie(response);
        return ResponseEntity.ok(
                new DefaultResponse(LocalDateTime.now().toString(), "Logged out successfully", 200)
        );
    }

    /**
     * Deletes the currently authenticated user account,
     * removes their data, clears the auth cookie, and sends a confirmation email.
     *
     * @param user     authenticated user
     * @param response HTTP response to clear cookie
     * @return HTTP 204 No Content
     */
    @Operation(
            summary = "Delete user account",
            description = "Deletes the authenticated user's account and clears their authentication cookie. Sends a confirmation email upon deletion."
    )
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal User user,
            @RequestBody DeleteAccountRequest deleteAccountRequest,
            HttpServletResponse response
    ) {
        authService.deleteUser(user, deleteAccountRequest);
        cookieHelper.clearAuthCookie(response);
        return ResponseEntity.noContent().build();
    }

    /**
     * Generates and returns a verification code for a user's email.
     *
     * @param email user email address
     * @return generated verification code
     */
    @Operation(
            summary = "Generate verification code",
            description = "Generates a new verification code for the provided email address."
    )
    @PostMapping("/resend-code")
    public ResponseEntity<DefaultResponse> resendCode(@RequestParam String email) {
        authService.resendCode(email);

        return ResponseEntity.ok(
                new DefaultResponse(
                        LocalDateTime.now().toString(),
                        "A new verification code has been sent to your email.",
                        200
                )
        );
    }

    /**
     * Verifies an email verification code.
     *
     * @param email user email
     * @param code  verification code
     * @return HTTP 200 if verification successful
     */
    @Operation(
            summary = "Verify email code",
            description = "Validates the provided verification code for the specified email."
    )
    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyCode(@RequestParam String email, @RequestParam String code, HttpServletResponse response) {
        User user = userHelper.getUserByEmail(email);

        authService.verifyCode(user, code);
        cookieHelper.setAuthCookie(user, response);

        return ResponseEntity.ok().build();
    }

    /**
     * Sends a password reset verification code to the given email.
     *
     * @param forgotPasswordRequest user's email
     * @return confirmation message
     */
    @Operation(
            summary = "Request password reset",
            description = "Sends a password reset verification code to the user's email address."
    )
    @PostMapping("/password/request")
    public ResponseEntity<DefaultResponse> requestPasswordChange(@NotNull @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        authService.requestPasswordChange(forgotPasswordRequest.email());
        return ResponseEntity.ok(new DefaultResponse(
                LocalDateTime.now().toString(),
                "A new verification code has been sent to your email.",
                200
        ));
    }

    /**
     * Confirms a password reset by verifying the code and updating the password.
     *
     * @param confirmChangePasswordRequest code with new password
     * @return confirmation message
     */
    @Operation(
            summary = "Confirm password reset",
            description = "Verifies the reset code sent by email and updates the user's password."
    )
    @PostMapping("/password/confirm")
    public ResponseEntity<DefaultResponse> confirmPasswordChange(
            @NotNull @RequestBody ConfirmChangePasswordRequest confirmChangePasswordRequest
    ) {
        authService.confirmPasswordChange(confirmChangePasswordRequest.code(), confirmChangePasswordRequest.newPassword());

        return ResponseEntity.ok(new DefaultResponse(
                LocalDateTime.now().toString(),
                "Password successfully changed.",
                200
        ));
    }

    /**
     * Changes the password of a logged-in user after verifying the old password.
     *
     * @param user    authenticated user
     * @param request contains old and new password
     * @return success message
     */
    @Operation(
            summary = "Change password (logged-in user)",
            description = "Allows a logged-in user to change their password after validating their current password."
    )
    @PostMapping("/password/change")
    public ResponseEntity<DefaultResponse> changePasswordLoggedIn(
            @AuthenticationPrincipal User user,
            @NotNull @RequestBody ChangePasswordRequest request
    ) {
        authService.changePasswordLoggedIn(user, request.oldPassword(), request.newPassword());

        return ResponseEntity.ok(new DefaultResponse(
                LocalDateTime.now().toString(),
                "Password successfully changed.",
                200
        ));
    }

    /**
     * Endpoint to update user's data
     *
     * @param user                 authenticated user
     * @param updateProfileRequest contains data used to update user's profile
     * @return success message
     */
    @Operation(
            summary = "Endpoint to update user's data",
            description = "Endpoint to update user's data"
    )
    @PatchMapping("/update")
    public ResponseEntity<UserDTO> updateTwoFactor(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        var response = authService.updateProfile(user, updateProfileRequest);

        String profilePhotoUrl = imageService.getProfilePhotoUrl(user);
        List<ImageResponse> exerciseImages = imageService.getExerciseImages(user.getId());

        return ResponseEntity.ok(UserDTO.fromEntity(response, profilePhotoUrl, exerciseImages, cronExpression));
    }
}