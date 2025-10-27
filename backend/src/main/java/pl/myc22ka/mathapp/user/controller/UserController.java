package pl.myc22ka.mathapp.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.myc22ka.mathapp.user.dto.UpdateUserRoleRequest;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

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
