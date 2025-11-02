package pl.myc22ka.mathapp.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import pl.myc22ka.mathapp.user.model.Role;

/**
 * Request DTO for updating a user's role.
 * Used in the UserController PATCH endpoint to change the role of a user.
 *
 * @param role the new role to assign to the user
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Schema(description = "Request for changing user role")
public record UpdateUserRoleRequest(
        @Schema(description = "New role for the user", example = "ADMIN")
        Role role
) {
}
