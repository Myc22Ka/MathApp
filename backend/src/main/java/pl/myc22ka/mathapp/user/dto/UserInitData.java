package pl.myc22ka.mathapp.user.dto;

import pl.myc22ka.mathapp.user.model.Role;
import pl.myc22ka.mathapp.user.model.User;

/**
 * DTO dla danych użytkownika z JSON-a (zawiera ścieżkę do zdjęcia).
 */
public record UserInitData(
        String login,
        String firstname,
        String lastname,
        String email,
        String password,
        Role role,
        String profilePhotoPath
) {
    public User toUser() {
        return User.builder()
                .login(this.login)
                .firstname(this.firstname)
                .lastname(this.lastname)
                .email(this.email)
                .password(this.password)
                .role(this.role)
                .verified(true)
                .twoFactorEnabled(false)
                .notificationsEnabled(true)
                .points(0.0)
                .streak(0)
                .level(1)
                .dailyTasksCompleted(0)
                .build();
    }
}
