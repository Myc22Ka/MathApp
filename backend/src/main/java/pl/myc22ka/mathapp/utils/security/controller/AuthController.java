package pl.myc22ka.mathapp.utils.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exceptions.DefaultResponse;
import pl.myc22ka.mathapp.s3.dto.ImageResponse;
import pl.myc22ka.mathapp.user.dto.UserDTO;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.service.UserImageService;
import pl.myc22ka.mathapp.utils.security.component.helper.CookieHelper;
import pl.myc22ka.mathapp.utils.security.dto.ChangePasswordRequest;
import pl.myc22ka.mathapp.utils.security.dto.LoginRequest;
import pl.myc22ka.mathapp.utils.security.dto.RegisterRequest;
import pl.myc22ka.mathapp.utils.security.dto.TwoFactorRequest;
import pl.myc22ka.mathapp.utils.security.service.AuthService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for authentication and user management.
 * Provides endpoints for user registration, login, logout,
 * password management, and two-factor authentication.
 *
 * @author Myc22Ka
 * @version 1.0.2
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
                        imageService.getProfilePhotoUrl(user),
                        null
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
            @NotNull HttpServletResponse response
    ) {
        User user = authService.login(loginRequest);
        cookieHelper.setAuthCookie(user, response);

        String profilePhotoUrl = imageService.getProfilePhotoUrl(user);
        List<ImageResponse> exerciseImages = imageService.getExerciseImages(user.getId());

        return ResponseEntity.ok(UserDTO.fromEntity(user, profilePhotoUrl, exerciseImages));
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
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String profilePhotoUrl = imageService.getProfilePhotoUrl(user);
        List<ImageResponse> exerciseImages = imageService.getExerciseImages(user.getId());

        return ResponseEntity.ok(UserDTO.fromEntity(user, profilePhotoUrl, exerciseImages));
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
            HttpServletResponse response
    ) {
        authService.deleteUser(user);
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
    @PostMapping("/generate-code")
    public ResponseEntity<String> generateCode(@RequestParam String email) {
        String code = authService.generateCode(email);
        return ResponseEntity.ok(code);
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
    public ResponseEntity<Void> verifyCode(@RequestParam String email, @RequestParam String code) {
        authService.verifyCode(email, code);
        return ResponseEntity.ok().build();
    }

    /**
     * Sends a password reset verification code to the given email.
     *
     * @param email user email
     * @return confirmation message
     */
    @Operation(
            summary = "Request password reset",
            description = "Sends a password reset verification code to the user's email address."
    )
    @PostMapping("/password/request")
    public ResponseEntity<String> requestPasswordChange(@RequestParam String email) {
        authService.requestPasswordChange(email);
        return ResponseEntity.ok("Verification code sent to email.");
    }

    /**
     * Confirms a password reset by verifying the code and updating the password.
     *
     * @param email       user email
     * @param code        verification code
     * @param newPassword new password to set
     * @return confirmation message
     */
    @Operation(
            summary = "Confirm password reset",
            description = "Verifies the reset code sent by email and updates the user's password."
    )
    @PostMapping("/password/confirm")
    public ResponseEntity<String> confirmPasswordChange(
            @RequestParam String email,
            @RequestParam String code,
            @RequestParam String newPassword
    ) {
        authService.confirmPasswordChange(email, code, newPassword);
        return ResponseEntity.ok("Password successfully changed.");
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
    public ResponseEntity<String> changePasswordLoggedIn(
            @AuthenticationPrincipal User user,
            @NotNull @RequestBody ChangePasswordRequest request
    ) {
        authService.changePasswordLoggedIn(user, request.oldPassword(), request.newPassword());
        return ResponseEntity.ok("Password successfully changed.");
    }

    /**
     * Enables or disables two-factor authentication (2FA) for the authenticated user.
     *
     * @param user    authenticated user
     * @param request contains desired 2FA status
     * @return confirmation message with updated status
     */
    @Operation(
            summary = "Update two-factor authentication (2FA) status",
            description = "Enables or disables two-factor authentication for the logged-in user."
    )
    @PostMapping("/2fa")
    public ResponseEntity<DefaultResponse> updateTwoFactor(
            @AuthenticationPrincipal User user,
            @RequestBody TwoFactorRequest request
    ) {
        authService.updateTwoFactorStatus(user, request);

        String status = request.enabled() ? "enabled" : "disabled";
        return ResponseEntity.ok(
                new DefaultResponse(LocalDate.now().toString(), "Two-factor authentication " + status, 200)
        );
    }
}