package pl.myc22ka.mathapp.utils.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request payload for delete a user's account.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 06.11.2025
 */
@Schema(description = "DTO to remove account")
public record DeleteAccountRequest(

        @Schema(
                description = "The user's password.",
                example = "String123!"
        )
        String password
) {
}