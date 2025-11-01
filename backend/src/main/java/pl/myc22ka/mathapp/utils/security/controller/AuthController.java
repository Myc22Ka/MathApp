package pl.myc22ka.mathapp.utils.security.controller;

import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserImageService imageService;
    private final CookieHelper cookieHelper;

    /**
     * Registers a new user, generates JWT token, sets it in HttpOnly cookie,
     * and returns the saved User entity.
     *
     * @param registerRequest the registration data sent by client
     * @param response        the HTTP response to add the cookie
     * @return the saved User entity
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @RequestBody RegisterRequest registerRequest,
            @NotNull HttpServletResponse response
    ) {
        User user = authService.register(registerRequest);

        cookieHelper.setAuthCookie(user, response);

        return ResponseEntity.ok(UserDTO.fromEntity(user, imageService.getProfilePhotoUrl(user), null));
    }

    @PostMapping("/sign-in")
    @Operation(summary = "User login")
    public ResponseEntity<UserDTO> login(
            @RequestBody LoginRequest loginRequest,
            @NotNull HttpServletResponse response
    ) {
        User user = authService.login(loginRequest);
        cookieHelper.setAuthCookie(user, response);

        // Załaduj zdjęcia oddzielnie
        String profilePhotoUrl = imageService.getProfilePhotoUrl(user);
        List<ImageResponse> exerciseImages = imageService.getExerciseImages(user.getId());

        return ResponseEntity.ok(
                UserDTO.fromEntity(
                        user,
                        profilePhotoUrl,
                        exerciseImages
                )
        );
    }

    // ====== GET CURRENT USER ======
    @GetMapping("/me")
    @Operation(
            summary = "Get current user info",
            description = "Returns the currently authenticated user's data including profile and exercise images."
    )
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Załaduj zdjęcia oddzielnie - brak problemu z LazyInitializationException
        String profilePhotoUrl = imageService.getProfilePhotoUrl(user);
        List<ImageResponse> exerciseImages = imageService.getExerciseImages(user.getId());

        return ResponseEntity.ok(
                UserDTO.fromEntity(
                        user,
                        profilePhotoUrl,
                        exerciseImages
                )
        );
    }

    // ====== SIGN OUT ======
    @PostMapping("/sign-out")
    @Operation(summary = "User logout")
    public ResponseEntity<DefaultResponse> logout(
            @NotNull HttpServletResponse response
    ) {
        cookieHelper.clearAuthCookie(response);
        return ResponseEntity.ok(
                new DefaultResponse(
                        LocalDateTime.now().toString(),
                        "Logged out successfully",
                        200
                )
        );
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal User user,
            HttpServletResponse response
    ) {
        authService.deleteUser(user);

        cookieHelper.clearAuthCookie(response);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate-code")
    public ResponseEntity<String> generateCode(@RequestParam String email) {
        String code = authService.generateCode(email);

        return ResponseEntity.ok(code);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyCode(@RequestParam String email, @RequestParam String code) {
        authService.verifyCode(email, code);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/request")
    public ResponseEntity<String> requestPasswordChange(@RequestParam String email) {
        authService.requestPasswordChange(email);
        return ResponseEntity.ok("Verification code sent to email.");
    }

    @PostMapping("/password/confirm")
    public ResponseEntity<String> confirmPasswordChange(@RequestParam String email,
                                                        @RequestParam String code,
                                                        @RequestParam String newPassword) {
        authService.confirmPasswordChange(email, code, newPassword);
        return ResponseEntity.ok("Password successfully changed.");
    }

    @PostMapping("/password/change")
    public ResponseEntity<String> changePasswordLoggedIn(@AuthenticationPrincipal User user,
                                                         @NotNull @RequestBody ChangePasswordRequest request) {
        authService.changePasswordLoggedIn(user, request.oldPassword(), request.newPassword());
        return ResponseEntity.ok("Password successfully changed.");
    }

    @PostMapping("/2fa")
    public ResponseEntity<DefaultResponse> updateTwoFactor(@AuthenticationPrincipal User user, @RequestBody TwoFactorRequest request) {
        authService.updateTwoFactorStatus(user, request);

        String status = request.enabled() ? "enabled" : "disabled";
        return ResponseEntity.ok(
                new DefaultResponse(LocalDate.now().toString(), "Two-factor authentication " + status, 200)
        );
    }
}
