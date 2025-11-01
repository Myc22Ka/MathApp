package pl.myc22ka.mathapp.utils.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request body for enabling or disabling two-factor authentication (2FA).
 */
@Schema(description = "Request to enable or disable two-factor authentication for the user.")
public record TwoFactorRequest(

        @Schema(
                description = "Whether two-factor authentication should be enabled (true) or disabled (false).",
                example = "true"
        )
        Boolean enabled
) {}
