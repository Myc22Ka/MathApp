package pl.myc22ka.mathapp.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Standard API error response.
 * <p>
 * Used to return a simple error message when an exception or invalid request occurs.
 *
 * @param error message describing the error
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 2025-10-17
 */
@Schema(description = "Error response structure for API endpoints",
        example = "{ \"error\": \"Invalid request data\" }")
public record ErrorResponse (
        @Schema(description = "Timestamp of the response", example = "2025-10-17T15:30:00Z")
        String timestamp,

        @Schema(description = "Message describing the response", example = "Operation successful")
        String message,

        @Schema(description = "HTTP status code", example = "200")
        int status
) {}
