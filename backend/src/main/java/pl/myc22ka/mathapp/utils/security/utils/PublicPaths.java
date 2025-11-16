package pl.myc22ka.mathapp.utils.security.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.AntPathMatcher;

import java.util.List;

public final class PublicPaths {

    private PublicPaths() {}

    public static final List<String> PATHS = List.of(
            "/login",
            "/auth/register",
            "/auth/sign-in",
            "/auth/verify-code",
            "/auth/resend-code",
            "/auth/password/request",
            "/auth/password/confirm",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/uploads/**",
            "/v3/api-docs/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/api/topics/**",
            "/login/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**"
    );

    /**
     * Checks if a given path matches any of the public endpoints.
     *
     * @param path        the request URI
     * @param pathMatcher the AntPathMatcher for pattern matching
     * @return true if the path is public, false otherwise
     */
    public static boolean isPublic(String path, AntPathMatcher pathMatcher) {
        return PATHS.stream().anyMatch(p -> pathMatcher.match(p, path));
    }

    /**
     * Gets the list as a String array for use with requestMatchers.
     *
     * @return array of public paths
     */
    @NotNull
    public static String[] getPathsArray() {
        return PATHS.toArray(new String[0]);
    }
}
