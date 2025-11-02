package pl.myc22ka.mathapp.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.user.dto.UpdateUserRoleRequest;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.service.UserService;

import java.util.List;

/**
 * REST controller for managing users.
 * Provides endpoints for retrieving users and updating user roles.
 * Access to certain operations is restricted to administrators.
 */
@Tag(name = "User Management", description = "Endpoints for managing users and their roles")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a list of all users.
     * This operation is restricted to users with the ADMIN role.
     *
     * @return a list of all User entities
     */
    @Operation(summary = "Get all users (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Updates the role of a specific user.
     * This operation is restricted to users with the ADMIN role.
     *
     * @param id      the ID of the user to update
     * @param request the request containing the new role
     * @return the updated User entity
     */
    @Operation(summary = "Update user role (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public User updateUserRole(
            @PathVariable Long id,
            @NotNull @RequestBody UpdateUserRoleRequest request
    ) {
        return userService.updateUserRole(id, request.role());
    }
}
