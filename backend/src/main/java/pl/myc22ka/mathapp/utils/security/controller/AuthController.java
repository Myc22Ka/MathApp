package pl.myc22ka.mathapp.utils.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.user.dto.UserDTO;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.utils.security.component.helper.CookieHelper;
import pl.myc22ka.mathapp.utils.security.dto.LoginRequest;
import pl.myc22ka.mathapp.utils.security.dto.RegisterRequest;
import pl.myc22ka.mathapp.utils.security.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieHelper cookieHelper;

    /**
     * Registers a new user, generates JWT token, sets it in HttpOnly cookie,
     * and returns the saved User entity.
     *
     * @param registerRequest the registration data sent by client
     * @param response the HTTP response to add the cookie
     * @return the saved User entity
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @RequestBody RegisterRequest registerRequest,
            @NotNull HttpServletResponse response
    ) {
        User user = authService.register(registerRequest);
        cookieHelper.setAuthCookie(user, response);

        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserDTO> login(
            @RequestBody LoginRequest loginRequest,
            @NotNull HttpServletResponse response
    ) {
        User user = authService.login(loginRequest);
        cookieHelper.setAuthCookie(user, response);

        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }

    @PostMapping("/sign-out")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Sign out",
            description = "Logs out the current user by clearing the login cookie."
    )
    public void signOut(@NotNull HttpServletResponse response) {
        cookieHelper.clearAuthCookie(response);
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
}
