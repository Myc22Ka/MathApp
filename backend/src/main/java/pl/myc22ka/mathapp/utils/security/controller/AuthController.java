package pl.myc22ka.mathapp.utils.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.exceptions.custom.UnauthorizedException;
import pl.myc22ka.mathapp.user.dto.UserDTO;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.utils.security.component.CookieProvider;
import pl.myc22ka.mathapp.utils.security.component.JwtProvider;
import pl.myc22ka.mathapp.utils.security.dto.LoginRequest;
import pl.myc22ka.mathapp.utils.security.dto.RegisterRequest;
import pl.myc22ka.mathapp.utils.security.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieProvider cookieProvider;
    private final JwtProvider jwtProvider;

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
        UserDTO savedUser = authService.register(registerRequest);

        String jwtToken = jwtProvider.generateToken(savedUser.id());

        ResponseCookie cookie = cookieProvider.createTokenCookie(jwtToken);
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserDTO> login(
            @RequestBody LoginRequest loginRequest,
            @NotNull HttpServletResponse response
    ) {
        UserDTO user = authService.login(loginRequest);
        String jwtToken = jwtProvider.generateToken(user.id());
        ResponseCookie cookie = cookieProvider.createTokenCookie(jwtToken);
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal User user,
            HttpServletResponse response
    ) {
        if (user == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        authService.deleteUser(user);

        ResponseCookie cookie = cookieProvider.clearTokenCookie();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.noContent().build();
    }
}
