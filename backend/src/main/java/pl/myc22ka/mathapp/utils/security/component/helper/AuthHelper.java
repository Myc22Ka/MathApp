package pl.myc22ka.mathapp.utils.security.component.helper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.utils.security.component.CookieProvider;
import pl.myc22ka.mathapp.utils.security.component.JwtProvider;

@Component
@RequiredArgsConstructor
public class AuthHelper {

    private final JwtProvider jwtProvider;
    private final CookieProvider cookieProvider;

    /**
     * Generates JWT token for given user and sets it as HttpOnly cookie in response.
     */
    public void setAuthCookie(@NotNull UserDetails userDetails, @NotNull HttpServletResponse response) {
        String jwtToken = jwtProvider.generateToken(userDetails);
        ResponseCookie cookie = cookieProvider.createTokenCookie(jwtToken);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Clears authentication cookie.
     */
    public void clearAuthCookie(@NotNull HttpServletResponse response) {
        ResponseCookie expiredCookie = cookieProvider.clearTokenCookie();
        response.setHeader("Set-Cookie", expiredCookie.toString());
    }
}
