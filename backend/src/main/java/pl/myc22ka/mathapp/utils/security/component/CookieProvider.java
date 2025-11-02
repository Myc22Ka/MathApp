package pl.myc22ka.mathapp.utils.security.component;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import pl.myc22ka.mathapp.exceptions.custom.InvalidTokenException;
import pl.myc22ka.mathapp.exceptions.custom.CookiesNotFoundException;

/**
 * Component responsible for handling JWT cookies in the application.
 * Provides methods to create, clear, and extract JWT tokens from cookies.
 * <p>
 * Uses secure, HttpOnly cookies with SameSite set to 'None'.
 * Expiration time is configured to 1 hour.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Component
@RequiredArgsConstructor
public class CookieProvider {

    @Value("${spring.security.cookie.name}")
    private String cookieName;

    @Value("${spring.security.cookie.expiration}")
    private long expirationSeconds;

    @Value("${spring.security.cookie.path}")
    private String cookiePath;

    @Value("${spring.security.cookie.same-site}")
    private String sameSite;

    @Value("${spring.security.cookie.http-only}")
    private boolean httpOnly;

    @Value("${spring.security.cookie.secure}")
    private boolean secure;
    /**
     * Creates a ResponseCookie containing the JWT token.
     * The cookie is HttpOnly, secure, and has a max age of 1 hour.
     *
     * @param jwtToken the JWT token to store in the cookie
     * @return the ResponseCookie with the JWT token
     */
    public ResponseCookie createTokenCookie(String jwtToken) {
        return ResponseCookie.from(cookieName, jwtToken)
                .httpOnly(httpOnly)
                .secure(secure)
                .sameSite(sameSite)
                .path(cookiePath)
                .maxAge(Duration.ofSeconds(expirationSeconds))
                .build();
    }

    /**
     * Creates a ResponseCookie that clears the JWT token (for logout purposes).
     * The cookie value is set to an empty string and max age is 0.
     *
     * @return the ResponseCookie that clears the JWT token
     */
    public ResponseCookie clearTokenCookie() {
        return ResponseCookie.from(cookieName, "")
                .httpOnly(httpOnly)
                .secure(secure)
                .sameSite(sameSite)
                .path(cookiePath)
                .maxAge(Duration.ZERO)
                .build();
    }

    /**
     * Extracts the JWT token from an array of cookies.
     * Throws an exception if the token is missing, null, or empty.
     *
     * @param cookies the array of cookies from the HTTP request
     * @return the JWT token
     * @throws CookiesNotFoundException if no cookies are present or auth token cookie is missing
     * @throws InvalidTokenException    if the token is null or empty
     */
    public String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new CookiesNotFoundException("No cookies present in request");
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                String token = cookie.getValue();
                if (token == null || token.isBlank()) {
                    throw new InvalidTokenException("Token is null or empty");
                }
                return token;
            }
        }

        throw new CookiesNotFoundException("Auth token cookie not found");
    }
}

