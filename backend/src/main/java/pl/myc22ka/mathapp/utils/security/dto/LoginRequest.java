package pl.myc22ka.mathapp.utils.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request payload for user login.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Schema(description = "Request body for user authentication.")
public record LoginRequest(

        @Schema(
                description = "User's email address used for login.",
                example = "user@example.com"
        )
        String email,

        @Schema(
                description = "User's password.",
                example = "MySecurePassword123!"
        )
        String password
) {}
