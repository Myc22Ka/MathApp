package pl.myc22ka.mathapp.utils.security.dto;

public record RegisterRequest(
        String login,
        String email,
        String password
) {}