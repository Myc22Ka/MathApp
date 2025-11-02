package pl.myc22ka.mathapp.utils.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request payload for changing a user's password.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Schema(description = "Request to change the user's password.")
public record ChangePasswordRequest(

        @Schema(
                description = "The user's current password.",
                example = "OldPassword123!"
        )
        String oldPassword,

        @Schema(
                description = "The new password to set. Must meet security requirements.",
                example = "NewStrongPassword456!"
        )
        String newPassword
) {
}
