package by.youngliqui.bookstorageservice.exception.handler;

import by.youngliqui.bookstorageservice.dto.ExceptionResponse;
import by.youngliqui.bookstorageservice.exception.abstr.ResourceAlreadyExistsException;
import by.youngliqui.bookstorageservice.exception.abstr.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ExceptionResponse handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return buildExceptionResponse(NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ExceptionResponse handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        log.error(e.getMessage(), e);
        return buildExceptionResponse(CONFLICT, e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ExceptionResponse handleExpiredJwt(ExpiredJwtException e) {
        log.warn("JWT expired: {}", e.getMessage());
        return buildExceptionResponse(UNAUTHORIZED, "JWT token has expired");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ExceptionResponse handleForbiddenExceptions(AccessDeniedException e) {
        log.warn("Access denied: {}", e.getMessage());
        return buildExceptionResponse(FORBIDDEN, "Access denied");
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handleMalformedJwt(MalformedJwtException e) {
        log.warn("Malformed JWT: {}", e.getMessage());
        return buildExceptionResponse(BAD_REQUEST, "JWT token is malformed");
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handleValidationExceptions(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder("Validation errors: ");

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessagePart = error.getDefaultMessage();
            errorMessage.append(fieldName).append(":").append(errorMessagePart).append("; ");
        });
        log.error(errorMessage.toString(), e);

        return buildExceptionResponse(BAD_REQUEST, errorMessage.toString());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleGenericException(Exception e) {
        String exceptionMessage = "An unexpected error occurred: " + e.getMessage();
        log.error(exceptionMessage, e);

        return buildExceptionResponse(INTERNAL_SERVER_ERROR, exceptionMessage);
    }


    private ExceptionResponse buildExceptionResponse(HttpStatus status, String message) {
        return ExceptionResponse.builder()
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
