package pl.myc22ka.mathapp.utils.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request payload for changing a user's password.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 06.11.2025
 */
@Schema(description = "Request to send link to change password.")
public record ForgotPasswordRequest(

        @Schema(
                description = "The user's email.",
                example = "example@domain.com"
        )
        String email
) {
}
