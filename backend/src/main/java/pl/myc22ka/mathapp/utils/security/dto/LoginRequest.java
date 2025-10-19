package pl.myc22ka.mathapp.utils.security.dto;

public record LoginRequest(
        String email,
        String password
) {}
