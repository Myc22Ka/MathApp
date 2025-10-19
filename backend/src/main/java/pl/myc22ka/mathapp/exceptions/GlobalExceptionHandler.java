package pl.myc22ka.mathapp.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;


import jakarta.persistence.EntityNotFoundException;
import pl.myc22ka.mathapp.exceptions.custom.PromptValidatorException;
import pl.myc22ka.mathapp.exceptions.custom.TemplateAlreadyExistsException;
import pl.myc22ka.mathapp.exceptions.custom.UserException;
import pl.myc22ka.mathapp.exceptions.custom.VariantTextMismatch;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEntityNotFoundException(@NotNull EntityNotFoundException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadCredentialsException(@NotNull BadCredentialsException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(@NotNull IllegalArgumentException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(PromptValidatorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePromptValidatorException(@NotNull PromptValidatorException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(TemplateAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTemplateExists(@NotNull TemplateAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(@NotNull DataIntegrityViolationException ex) {

        String message = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        if (message.contains("unique") || message.contains("duplicate") || message.contains("constraint")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Resource with this data already exists"));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Data integrity violation"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(@NotNull MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ErrorResponse(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(@NotNull ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        return new ErrorResponse(message);
    }

    @ExceptionHandler(VariantTextMismatch.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleVariantTextMismatch(@NotNull VariantTextMismatch ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserException(@NotNull UserException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
