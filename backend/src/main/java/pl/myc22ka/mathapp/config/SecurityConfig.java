package pl.myc22ka.mathapp.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.utils.security.component.JwtAuthenticationFilter;
import pl.myc22ka.mathapp.utils.security.component.helper.CookieHelper;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Publicly accessible endpoints that do not require authentication.
     * Includes authentication endpoints and Swagger documentation paths.
     */
    private static final String[] WHITE_LIST_URL = {"/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/api/topics/**",
            "/login/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};

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
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL)
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
}
