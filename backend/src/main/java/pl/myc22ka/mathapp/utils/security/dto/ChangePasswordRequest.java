package pl.myc22ka.mathapp.utils.security.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {
}
