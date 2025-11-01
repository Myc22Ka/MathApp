package pl.myc22ka.mathapp.utils.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import pl.myc22ka.mathapp.utils.security.validator.Password;

/**
 * Request body for user registration.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Schema(description = "User registration request")
public record RegisterRequest(

        @NotBlank
        @Schema(description = "Unique user login", example = "math_wizard")
        String login,

        @NotBlank
        @Email
        @Schema(description = "User email address", example = "user@example.com")
        String email,

        @NotBlank
        @Password
        @Schema(description = "User password (min 8 characters)", example = "SecurePass123!")
        String password
) {}