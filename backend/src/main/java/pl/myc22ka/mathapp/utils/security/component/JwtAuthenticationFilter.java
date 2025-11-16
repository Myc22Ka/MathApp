package pl.myc22ka.mathapp.utils.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.myc22ka.mathapp.exceptions.ErrorResponse;
import pl.myc22ka.mathapp.exceptions.custom.CookiesNotFoundException;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.utils.security.utils.PublicPaths;

import java.io.IOException;
import java.time.Instant;

/**
 * Filter that authenticates requests using JWT tokens stored in cookies.
 * It checks if the request path is public; if not, it extracts the JWT token from cookies,
 * validates it, and sets the authentication in the security context.
 * If authentication fails, it responds with a 401 Unauthorized status and an error message.
 *
 * @author Myc22Ka
 * @version 1.0.1
 * @since 01.11.2025
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CookieProvider cookieProvider;
    private final UserHelper userHelper;
    private final JwtProvider jwtProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (PublicPaths.isPublic(path, pathMatcher)) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();

        try {
            if (cookies == null) throw new CookiesNotFoundException("No cookies present in request");

            String token = cookieProvider.extractTokenFromCookies(cookies);
            if (token == null) throw new CookiesNotFoundException("Auth token cookie not found");

            String username = jwtProvider.extractUsername(token);
            if (username == null) throw new JwtException("Token subject is null or invalid");

            User user = userHelper.getUserByEmail(username);

            if (!jwtProvider.isTokenValid(token, user)) throw new JwtException("Token is invalid or expired");

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (CookiesNotFoundException e) {
            sendErrorResponse(response, HttpServletResponse.SC_NO_CONTENT, e.getMessage());
        }
        catch (UsernameNotFoundException | JwtException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected authentication error", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected authentication error");
        }
    }

    private void sendErrorResponse(@NotNull HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now().toString(),
                message,
                status
        );

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
}