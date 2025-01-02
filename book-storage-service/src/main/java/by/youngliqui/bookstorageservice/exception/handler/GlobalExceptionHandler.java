package by.youngliqui.bookstorageservice.exception.handler;

import by.youngliqui.bookstorageservice.dto.ExceptionResponse;
import by.youngliqui.bookstorageservice.exception.abstr.ResourceAlreadyExistsException;
import by.youngliqui.bookstorageservice.exception.abstr.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
