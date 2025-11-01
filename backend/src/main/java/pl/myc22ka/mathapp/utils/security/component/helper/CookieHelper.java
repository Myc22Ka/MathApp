package pl.myc22ka.mathapp.utils.security.component.helper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.utils.security.component.CookieProvider;
import pl.myc22ka.mathapp.utils.security.component.JwtProvider;

/**
 * Helper class for managing authentication cookies.
 * <p>
 * Handles setting and clearing the JWT token stored in an HttpOnly cookie.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Component
@RequiredArgsConstructor
public class CookieHelper {

    private final JwtProvider jwtProvider;
    private final CookieProvider cookieProvider;

    /**
     * Creates a JWT token for the specified user and adds it
     * to the response as an HttpOnly cookie.
     *
     * @param userDetails the authenticated user
     * @param response    the HTTP response to which the cookie will be added
     */
    public void setAuthCookie(@NotNull UserDetails userDetails, @NotNull HttpServletResponse response) {
        String jwtToken = jwtProvider.generateToken(userDetails);
        ResponseCookie cookie = cookieProvider.createTokenCookie(jwtToken);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Removes the authentication cookie by setting an expired one.
     *
     * @param response the HTTP response to which the expired cookie will be added
     */
    public void clearAuthCookie(@NotNull HttpServletResponse response) {
        ResponseCookie expiredCookie = cookieProvider.clearTokenCookie();
        response.setHeader("Set-Cookie", expiredCookie.toString());
    }
}
