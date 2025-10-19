package pl.myc22ka.mathapp.user.dto;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record UserDTO(
        Long id,
        String login,
        String firstname,
        String lastname,
        String email,
        String role,
        Integer points,
        Integer level,
        String photoUrl,
        String phoneNumber,
        String address,
        LocalDate dateOfBirth,
        String gender
) {

    @NotNull
    public static UserDTO fromEntity(@NotNull pl.myc22ka.mathapp.user.model.User user) {
        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole().name(),
                user.getPoints(),
                user.getLevel(),
                user.getPhotoUrl(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getGender()
        );
    }
}
