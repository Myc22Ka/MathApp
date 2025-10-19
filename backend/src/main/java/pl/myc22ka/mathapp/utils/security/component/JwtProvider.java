package pl.myc22ka.mathapp.utils.security.component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import pl.myc22ka.mathapp.utils.security.component.helper.JwtHelper;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtHelper jwtHelper;

    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        try {
            return jwtHelper.extractClaim(token, Claims::getSubject);
        } catch (JwtException e) {
            return null;
        }
    }

    public <T> T extractClaim(String token, @NotNull Function<Claims, T> claimsResolver) {
        return jwtHelper.extractClaim(token, claimsResolver);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return jwtHelper.buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public boolean isTokenValid(String token, @NotNull UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null
                && username.equals(userDetails.getUsername())
                && !jwtHelper.isTokenExpired(token);
    }
}

