package pl.myc22ka.mathapp.exceptions;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Standard API response structure.
 * <p>
 * Used to return messages, HTTP status, and timestamp for any endpoint.
 * Typically used in exception handlers or general API responses.
 *
 * @param timestamp ISO-8601 formatted timestamp of the response
 * @param message   message describing the response
 * @param status    HTTP status code
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Schema(description = "Default response structure for API endpoints")
public record DefaultResponse(
        @Schema(description = "Timestamp of the response", example = "2025-10-17T15:30:00Z")
        String timestamp,

        @Schema(description = "Message describing the response", example = "Operation successful")
        String message,

        @Schema(description = "HTTP status code", example = "200")
        int status
) {
}
