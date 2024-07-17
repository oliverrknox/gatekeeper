package net.gb.knox.gatekeeper.exception;

import net.gb.knox.gatekeeper.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException exception) {
        var response = new ErrorResponseDTO(exception.getMessage(), exception.getErrorsByField());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException() {
        var response = new ErrorResponseDTO("Something went wrong.");
        return ResponseEntity.internalServerError().body(response);
    }
}
