package pl.myc22ka.mathapp.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import pl.myc22ka.mathapp.user.model.Role;

@Schema(description = "Request for changing user role")
public record UpdateUserRoleRequest(
        @Schema(description = "New role for the user", example = "ADMIN")
        Role role
) {}
