package pl.myc22ka.mathapp.utils.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling OAuth2 authentication flow.
 * Provides endpoints for login success and failure events.
 *
 * @author Myc22Ka
 * @version 1.0.2
 * @since 01.11.2025
 */
@RestController
@RequestMapping("/api/oauth2")
@Tag(name = "OAuth2 Authentication", description = "Endpoints for handling OAuth2 login success and failure")
public class OAuth2Controller {

    /**
     * Handles successful OAuth2 login events.
     * Returns basic information about the authenticated user
     * and the provider that was used for authentication.
     *
     * @param authentication the OAuth2 authentication token containing provider and user details
     * @return a message confirming successful login and displaying user email
     */
    @Operation(
            summary = "Handle successful OAuth2 login",
            description = "Returns provider name and user email after successful OAuth2 authentication."
    )
    @GetMapping("/success")
    public String success(@NotNull OAuth2AuthenticationToken authentication) {
        return "Zalogowano przez: " + authentication.getAuthorizedClientRegistrationId() +
                " jako " + authentication.getPrincipal().getAttributes().get("email");
    }

    /**
     * Handles failed OAuth2 login attempts.
     * Returns a simple error message indicating that the authentication failed.
     *
     * @return a message indicating OAuth2 login failure
     */
    @Operation(
            summary = "Handle OAuth2 login failure",
            description = "Returns an error message when OAuth2 authentication fails."
    )
    @GetMapping("/failure")
    public String failure() {
        return "Logowanie OAuth2 nie powiodło się.";
    }
}
