package pl.myc22ka.mathapp.utils.security.component;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import pl.myc22ka.mathapp.utils.security.component.helper.JwtHelper;

/**
 * Component responsible for generating, extracting and validating JWT tokens.
 * Provides integration with {@link JwtHelper} for claim extraction, token building
 * and expiration verification.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtHelper jwtHelper;

    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Extracts the username (subject) from the provided JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) or {@code null} if extraction fails
     */
    public String extractUsername(String token) {
        try {
            return jwtHelper.extractClaim(token, Claims::getSubject);
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * Generates a JWT token for the given user.
     * Uses an empty set of extra claims.
     *
     * @param userDetails the user for whom the token is generated
     * @return the generated JWT token string
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token for the given user, including extra claims.
     * The token will automatically include the expiration time defined by {@code jwtExpiration}.
     *
     * @param extraClaims  additional claims to include in the token payload
     * @param userDetails  the authenticated user
     * @return the generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return jwtHelper.buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Validates whether the given JWT token is valid for the specified user.
     * Checks if the username matches and the token has not expired.
     *
     * @param token        the JWT token string
     * @param userDetails  the user to validate against
     * @return {@code true} if the token is valid and not expired, otherwise {@code false}
     */
    public boolean isTokenValid(String token, @NotNull UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null
                && username.equals(userDetails.getUsername())
                && !jwtHelper.isTokenExpired(token);
    }
}

