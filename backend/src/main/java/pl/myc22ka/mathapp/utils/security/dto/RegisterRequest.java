package pl.myc22ka.mathapp.utils.security.dto;

import java.time.LocalDate;

public record RegisterRequest(
        String login,
        String firstname,
        String lastname,
        String email,
        String password,
        String gender,
        LocalDate dateOfBirth,
        String phoneNumber,
        String address,
        String photoUrl
) {}