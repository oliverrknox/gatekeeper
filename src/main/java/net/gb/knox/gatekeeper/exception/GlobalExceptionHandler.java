package net.gb.knox.gatekeeper.exception;

import net.gb.knox.gatekeeper.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException() {
        var response = new ErrorResponseDTO("Something went wrong.");
        return ResponseEntity.internalServerError().body(response);
    }
}
