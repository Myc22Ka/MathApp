package pl.myc22ka.mathapp.utils.security.dto;

import java.time.LocalDate;

public record UpdateProfileRequest(String firstname, String lastname, String phoneNumber, String address,
                                   LocalDate dateOfBirth, String gender, Boolean notificationsEnabled, Boolean twoFactorAuthEnabled) {
}
