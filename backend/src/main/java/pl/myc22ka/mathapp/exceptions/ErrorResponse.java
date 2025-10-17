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

        @Schema(description = "Error message describing what went wrong", example = "Invalid request data")
        String error
) {}
