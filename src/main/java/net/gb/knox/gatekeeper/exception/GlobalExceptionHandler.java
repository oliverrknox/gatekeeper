package net.gb.knox.gatekeeper.exception;

import net.gb.knox.gatekeeper.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException exception) {
        var response = new ErrorResponseDTO(exception.getMessage(), exception.getErrorsByField());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException exception) {
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException() {
        var response = new ErrorResponseDTO("Something went wrong.");
        return ResponseEntity.internalServerError().body(response);
    }
}
