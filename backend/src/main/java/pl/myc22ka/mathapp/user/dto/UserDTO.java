package pl.myc22ka.mathapp.user.dto;

import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.s3.dto.ImageResponse;
import pl.myc22ka.mathapp.user.model.User;

import java.time.LocalDate;
import java.util.List;

public record UserDTO(
        Long id,
        String login,
        String firstname,
        String lastname,
        String email,
        String role,
        Integer points,
        Integer level,
        String phoneNumber,
        String address,
        LocalDate dateOfBirth,
        String gender,
        String profilePhotoUrl,
        List<ImageResponse> exerciseImages
) {

    @NotNull
    public static UserDTO fromEntity(@NotNull User user, String profilePhotoUrl, List<ImageResponse> exerciseImages) {
        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole().name(),
                user.getPoints(),
                user.getLevel(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getGender(),
                profilePhotoUrl,
                exerciseImages
        );
    }
}
