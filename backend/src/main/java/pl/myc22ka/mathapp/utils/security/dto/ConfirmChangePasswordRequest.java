package pl.myc22ka.mathapp.utils.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to change the user's password with given code via email.")
public record ConfirmChangePasswordRequest(

        @Schema(
                description = "The user's current code",
                example = "123456"
        )
        String code,

        @Schema(
                description = "The new password to set. Must meet security requirements.",
                example = "NewStrongPassword456!"
        )
        String newPassword
) {
}