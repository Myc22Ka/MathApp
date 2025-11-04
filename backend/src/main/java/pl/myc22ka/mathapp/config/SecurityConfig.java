package pl.myc22ka.mathapp.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.utils.security.component.JwtAuthenticationFilter;
import pl.myc22ka.mathapp.utils.security.component.helper.CookieHelper;
import pl.myc22ka.mathapp.utils.security.utils.PublicPaths;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Security configuration class for the application.
 * <p>
 * Configures HTTP security, including CSRF protection, session management,
 * endpoint access rules, OAuth2 login handling, and JWT authentication.
 * </p>
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CookieHelper cookieHelper;
    private final UserHelper userHelper;

    @Value("${spring.frontend.url}")
    private String frontendUrl;
    // TODO: change this when frontend is ready

    /**
     * Configures the HTTP security filter chain for the application.
     * Sets up CSRF, session management, endpoint access rules, authentication, and logout behavior.
     *
     * @param http the HttpSecurity object used to customize security settings
     * @return the configured SecurityFilterChain
     * @throws Exception in case of misconfiguration
     */
    @Bean
    SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(req -> req
                        .requestMatchers(PublicPaths.getPathsArray())
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
                            String email = oauthUser.getAttribute("email");

                            User user = userHelper.createOAuth2User(email, oauthUser);

                            cookieHelper.setAuthCookie(user, response);

                            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
                            String redirectUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "http://localhost:8080/";

                            response.sendRedirect(redirectUrl);
                        })
                        .failureUrl("/api/oauth2/failure")
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the password encoder bean using BCrypt hashing algorithm.
     *
     * @return the PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
