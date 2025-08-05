package pl.myc22ka.mathapp.exceptions;

public record DefaultResponse(
        String timestamp,
        String message,
        int status
) {}
