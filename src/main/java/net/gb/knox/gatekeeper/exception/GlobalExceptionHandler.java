package net.gb.knox.gatekeeper.exception;

import net.gb.knox.gatekeeper.dto.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException exception, WebRequest webRequest) {
        logger.warn("UserNotFoundException | exception={} request={}", exception, webRequest.getDescription(true));

        var response = new ErrorResponseDTO(exception.getMessage(), exception.getErrorsByField());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        logger.warn("ValidationException | exception={} request={}", exception, webRequest.getDescription(true));

        var errorsByField = new HashMap<String, String>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            var fieldError = (FieldError) error;

            String fieldName = fieldError.getField();
            String fieldErrorMessage = fieldError.getDefaultMessage();

            errorsByField.put(fieldName, fieldErrorMessage);
        });

        var response = new ErrorResponseDTO("Validation failed.", errorsByField);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UnauthorisedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorisedException(UnauthorisedException exception, WebRequest webRequest) {
        logger.warn("UnauthorisedException | exception={} request={}", exception, webRequest.getDescription(true));

        var response = new ErrorResponseDTO(exception.getMessage(), exception.getErrorsByField());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception exception, WebRequest webRequest) {
        logger.error("Unknown Exception | exception={} request={}", exception, webRequest.getDescription(true));

        var response = new ErrorResponseDTO("Something went wrong.");
        return ResponseEntity.internalServerError().body(response);
    }
}
