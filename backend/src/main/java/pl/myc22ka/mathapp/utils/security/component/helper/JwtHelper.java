package pl.myc22ka.mathapp.utils.security.component.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for handling JWT operations such as token creation,
 * validation, and claim extraction.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Component
public class JwtHelper {

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    /**
     * Converts the Base64-encoded secret key into a {@link SecretKey} object
     * used for signing and verifying JWT tokens.
     */
    @NotNull
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts all claims (payload data) from a JWT token.
     *
     * @param token the JWT token
     * @return all claims contained in the token
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts a specific claim from the JWT token using a provided resolver.
     *
     * @param token          the JWT token
     * @param claimsResolver function to extract a specific claim
     * @return the resolved claim value
     */
    public <T> T extractClaim(String token, @NotNull Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Builds a new JWT token with the given claims and expiration time.
     *
     * @param extraClaims additional data to include in the token
     * @param userDetails the authenticated user details
     * @param expiration  token lifetime in milliseconds
     * @return the signed JWT token as a String
     */
    public String buildToken(
            Map<String, Object> extraClaims,
            @NotNull UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }
}