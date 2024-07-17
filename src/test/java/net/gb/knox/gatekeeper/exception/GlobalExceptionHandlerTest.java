package net.gb.knox.gatekeeper.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    private static final String MESSAGE = "Test exception message.";
    private static final Map<String, String> ERRORS_BY_FIELD = Map.of("Error key", "Error value");

    @BeforeEach()
    public void Setup() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleUserNotFoundException() {
        var userNotFoundException = new UserNotFoundException(MESSAGE, ERRORS_BY_FIELD);
        var responseEntity = globalExceptionHandler.handleUserNotFoundException(userNotFoundException);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MESSAGE, responseEntity.getBody().message());
        assertEquals(ERRORS_BY_FIELD, responseEntity.getBody().errorsByField());
    }

    @Test
    public void testHandleGenericException() {
        var responseEntity = globalExceptionHandler.handleGenericException();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Something went wrong.", responseEntity.getBody().message());
        assertNull(responseEntity.getBody().errorsByField());
    }
}
