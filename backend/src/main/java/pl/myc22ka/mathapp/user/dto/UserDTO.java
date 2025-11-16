package pl.myc22ka.mathapp.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.NotNull;
import pl.myc22ka.mathapp.exercise.daily.dto.DailyExercise;
import pl.myc22ka.mathapp.s3.dto.ImageResponse;
import pl.myc22ka.mathapp.user.model.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object representing a user.
 * Contains personal details, account statistics, and associated images.
 * Used for returning user information via REST endpoints.
 *
 * @author Myc22Ka
 * @version 1.0.5
 * @since 01.11.2025
 */
@Schema(description = "User Data Transfer Object containing personal info, stats, and exercise images")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
        @Schema(description = "User's unique identifier", example = "123")
        Long id,

        @Schema(description = "User's login name", example = "john_doe")
        String login,

        @Schema(description = "User's first name", example = "John")
        String firstname,

        @Schema(description = "User's last name", example = "Doe")
        String lastname,

        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "User's role", example = "STUDENT")
        String role,

        @Schema(description = "User's accumulated points", example = "150.5")
        Double points,

        @Schema(description = "User's current level", example = "3")
        Integer level,

        @Schema(description = "User's phone number", example = "+48123456789")
        String phoneNumber,

        @Schema(description = "User's address", example = "123 Main St, Warsaw")
        String address,

        DailyExercise dailyExercise,

        @Schema(description = "User's date of birth", example = "1990-01-01")
        LocalDate dateOfBirth,

        @Schema(description = "User's gender", example = "Male")
        String gender,

        @Schema(description = "URL of the user's profile photo", example = "https://example.com/photo.jpg")
        String profilePhotoUrl,

        @Schema(description = "List of images associated with user's exercises")
        List<ImageResponse> exerciseImages,

        @Schema(description = "User is verified by email", example = "false")
        Boolean verified,

        @Schema(description = "User has enabled 2FA", example = "false")
        Boolean twoFactorEnabled,

        @Schema(description = "User has enabled notifications", example = "false")
        Boolean notificationsEnabled
) {

    /**
     * Factory method for 2FA response - returns only ID and email.
     *
     * @param user the User entity
     * @return a UserDTO with only id and email, all other fields null
     */
    @NotNull
    public static UserDTO forTwoFactorAuth(@NotNull User user) {
        return new UserDTO(
                null,
                null, null, null,
                user.getEmail(),
                null, null, null, null, null, null, null, null, null, null,
                null, null, null
        );
    }

    /**
     * Factory method to create a UserDTO from a User entity.
     *
     * @param user            the User entity
     * @param profilePhotoUrl URL of the user's profile photo
     * @param exerciseImages  list of images associated with user's exercises
     * @param cronExpression  cron expression for daily exercises
     * @return a new UserDTO instance populated from the User entity
     */
    @NotNull
    public static UserDTO fromEntity(@NotNull User user, String profilePhotoUrl, List<ImageResponse> exerciseImages, String cronExpression) {
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
                DailyExercise.fromUser(user, cronExpression),
                user.getDateOfBirth(),
                user.getGender(),
                profilePhotoUrl,
                exerciseImages,
                user.getVerified(),
                user.getTwoFactorEnabled(),
                user.getNotificationsEnabled()
        );
    }
}