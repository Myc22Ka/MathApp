package pl.myc22ka.mathapp.utils.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import pl.myc22ka.mathapp.exceptions.ErrorResponse;
import pl.myc22ka.mathapp.exceptions.custom.CookiesNotFoundException;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CookieProvider cookieProvider;
    private final UserRepository userRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> PUBLIC_PATHS = List.of(
            "/login",
            "/auth/register",
            "/auth/register/availability",
            "/auth/sign-in",
            "/auth/reset-password/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/uploads/**",
            "/v3/api-docs/**");

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(p -> pathMatcher.match(p, path));

        if (isPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();

        try {
            if (cookies != null) {
                String username = cookieProvider.extractUsernameFromCookies(cookies);
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException | CookiesNotFoundException | UsernameNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            ErrorResponse errorResponse = new ErrorResponse("Unauthorized");
            String json = new ObjectMapper().writeValueAsString(errorResponse);

            response.getWriter().write(json);
        }
    }
}

