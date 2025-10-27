package pl.myc22ka.mathapp.utils.security.component;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import pl.myc22ka.mathapp.exceptions.custom.InvalidTokenException;
import pl.myc22ka.mathapp.exceptions.custom.CookiesNotFoundException;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    private final JwtProvider jwtProvider;

    private static final String COOKIE_NAME = "authToken";
    private static final Duration EXPIRATION = Duration.ofHours(1);
    private static final String COOKIE_PATH = "/";
    private static final String SAME_SITE = "None";
    private static final boolean HTTP_ONLY = true;
    private static final boolean SECURE = true;

    public ResponseCookie createTokenCookie(String jwtToken) {
        return ResponseCookie.from(COOKIE_NAME, jwtToken)
                .httpOnly(HTTP_ONLY)
                .secure(SECURE)
                .sameSite(SAME_SITE)
                .path(COOKIE_PATH)
                .maxAge(EXPIRATION)
                .build();
    }

    public ResponseCookie clearTokenCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(HTTP_ONLY)
                .secure(SECURE)
                .sameSite(SAME_SITE)
                .path(COOKIE_PATH)
                .maxAge(Duration.ZERO)
                .build();
    }

    public String extractUsernameFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new CookiesNotFoundException("No cookies present in request");
        }

        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                String token = cookie.getValue();
                String username = jwtProvider.extractUsername(token);
                if (username == null) {
                    throw new InvalidTokenException("Token subject is null or invalid");
                }
                return username; // teraz String zamiast Long
            }
        }

        throw new CookiesNotFoundException("Auth token cookie not found");
    }

}

